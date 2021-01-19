package cn.begonia.lucene.jaslucene.service.handler;

import cn.begonia.lucene.jaslucene.common.QueryCondition;
import cn.begonia.lucene.jaslucene.common.Result;
import cn.begonia.lucene.jaslucene.common.SearchType;
import cn.begonia.lucene.jaslucene.config.ContextProperties;
import cn.begonia.lucene.jaslucene.famatter.LuceneFormatter;
import cn.begonia.lucene.jaslucene.famatter.parser.RangeParser;
import cn.begonia.lucene.jaslucene.util.DateUtils;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 读数据查询含各类查询，查找索引服务
 * @data  20200813
 * **/
@Slf4j
@Service
public class LuceneReaderService {
    private FSDirectory  fsDirectory;
    private DirectoryReader directoryReader;
    private IndexWriter  indexWriter;
    private IndexSearcher indexSearcher;
    @Autowired
    ContextProperties properties;


    /** 新增查询数据 **/
    public Result  executeQuery(Query  query,int item){
        QueryCondition  queryCondition=new QueryCondition();
        return querySearch(query,queryCondition);
    }

    public  Result   executeQuery(Query query,QueryCondition  queryCondition){
        return querySearch(query,queryCondition);
    }

    /**
     * 表达式范围查询
     * @field
     * @param  expression 表达式
     * */
    public    Result  numericQuery(String field,String expression,QueryCondition queryCondition) throws ParseException {
        RangeParser parser=new RangeParser(Version.LUCENE_CURRENT,field,new StandardAnalyzer(Version.LUCENE_CURRENT));
        Query  query=parser.parse(expression);


        return executeQuery(query,queryCondition);
    }

    /**
     * 执行查询，并打印查询到的记录数
     * @param query
     * @throws IOException
     */
    @SuppressWarnings("all")
    public  Result  querySearch(Query query,QueryCondition condition){
        List<JSONObject> list=new ArrayList<>();
        try {
            SortField  rank=new SortField("rank",SortField.Type.INT,false);
            SortField  like=new SortField("like",SortField.Type.INT,true);
            SortField  date =new SortField("date",SortField.Type.LONG,true);
            SortField  focus =new SortField("focus",SortField.Type.INT,true);
            SortField  browse =new SortField("browse",SortField.Type.INT,true);
            SortField  comment =new SortField("comment",SortField.Type.INT,true);
            Sort  sort=new Sort(rank,like,date,focus,browse,comment);
            TopFieldCollector collector =  TopFieldCollector.create(sort, 10000, true, true, false, false);
            //TopScoreDocCollector collector = TopScoreDocCollector.create(10000, false);
            TopDocs docs=null;
            if(condition.getPage()==null||condition.getPage()==0){
                docs= indexSearcher.search(query,10000,sort);
            }else {
                indexSearcher.search(query,collector);
                docs =collector.topDocs(condition.getPage(),condition.getPageSize());
            }
            //打印查询到的记录数
            System.out.println("总共查询到" + docs.totalHits + "个文档");
            for (ScoreDoc scoreDoc : docs.scoreDocs) {
                JSONObject obj=new JSONObject();
                //取得对应的文档对象
                Document document = indexSearcher.doc(scoreDoc.doc);
              /*  List<IndexableField> list= document.getFields();
                for(IndexableField field:list){
                    //System.out.println(field.name());
                }*/
                obj.put("title",document.get("title"));
                obj.put("url",document.get("url"));
                obj.put("auth",document.get("auth"));
                obj.put("date",document.get("date"));
                obj.put("like",document.get("like"));
                obj.put("comment",document.get("comment"));
                obj.put("browse",document.get("browse"));
                obj.put("content",document.get("content"));
                obj.put("img",document.get("img"));
                obj.put("focus",document.get("focus"));
                obj.put("rank",document.get("rank"));
                list.add(obj);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.isOk(list);
    }


    /**
     * 分词查找
     * @param analyzer   选用的分词器
     * @param field  要分词查询的关键字
     * */
    public  void   analyzerQuery(Analyzer analyzer,String field,String  content){
        TokenStream  tokenStream=null;
        try {
            tokenStream=analyzer.tokenStream(field,new StringReader(content));
            CharTermAttribute charTermAttribute=tokenStream.addAttribute(CharTermAttribute.class);
            tokenStream.reset();
            while(tokenStream.incrementToken()){
                System.out.println(charTermAttribute.toString());
            }
            tokenStream.end();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                tokenStream.close();
                analyzer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 简单查询
     * @param key  field
     * @param value  condition
     * **/
    public  Result  termQuery(String key,String value,QueryCondition queryCondition){
        TermQuery  query=new TermQuery(new Term(key,value));
        return executeQuery(query,queryCondition);
    }

    /**
     * method
     * **/
    public   Result  termQuery(Term  term,QueryCondition queryCondition){
        return executeQuery(new TermQuery(term),queryCondition);
    }

    /***
     * * 多条件查询
     * <p>
     * BooleanQuery也是实际开发过程中经常使用的一种Query。
     * 它其实是一个组合的Query，在使用时可以把各种Query对象添加进去并标明它们之间的逻辑关系。
     * BooleanQuery本身来讲是一个布尔子句的容器，它提供了专门的API方法往其中添加子句，
     * 并标明它们之间的关系，以下代码为BooleanQuery提供的用于添加子句的API接口：
     */
    public  Result   multipleQuery(Map<String,Object> map,QueryCondition  queryCondition){
        List<Term> list=new ArrayList<>();
        for(Map.Entry<String,Object> ty:map.entrySet()){
            Term term=new Term(ty.getKey(),String.valueOf(ty.getValue()));
            list.add(term);
        }
        return  multipleQuery(list,queryCondition);
    }

    public  Result   multipleQuery(List<Term> list,QueryCondition queryCondition){
        // BooleanClause用于表示布尔查询子句关系的类，
        // 包 括：
        // BooleanClause.Occur.MUST，
        // BooleanClause.Occur.MUST_NOT，
        // BooleanClause.Occur.SHOULD。
        // 必须包含,不能包含,可以包含三种.有以下6种组合：
        //
        // 1．MUST和MUST：取得连个查询子句的交集。
        // 2．MUST和MUST_NOT：表示查询结果中不能包含MUST_NOT所对应得查询子句的检索结果。
        // 3．SHOULD与MUST_NOT：连用时，功能同MUST和MUST_NOT。
        // 4．SHOULD与MUST连用时，结果为MUST子句的检索结果,但是SHOULD可影响排序。
        // 5．SHOULD与SHOULD：表示“或”关系，最终检索结果为所有检索子句的并集。
        // 6．MUST_NOT和MUST_NOT：无意义，检索无结果。
        BooleanQuery  booleanQuery=new BooleanQuery();
        for(Term term:list){
            booleanQuery.add(new TermQuery(term), BooleanClause.Occur.SHOULD);
        }
        return executeQuery(booleanQuery,queryCondition);
    }

    /**
     * 匹配前缀
     * <p>
     * PrefixQuery用于匹配其索引开始以指定的字符串的文档。就是文档中存在xxx%
     * <p>
     * **/
    public  Result  prefixQuery(String key ,Object value,QueryCondition queryCondition){
        Query query=new PrefixQuery(new Term(key,String.valueOf(value)));
        return executeQuery(query,queryCondition);
    }


    /**
     * 短语搜索
     * <p>
     * 所谓PhraseQuery，就是通过短语来检索，比如我想查“big car”这个短语，
     * 那么如果待匹配的document的指定项里包含了"big car"这个短语，
     * 这个document就算匹配成功。可如果待匹配的句子里包含的是“big black car”，
     * 那么就无法匹配成功了，如果也想让这个匹配，就需要设定slop，
     * 先给出slop的概念：slop是指两个项的位置之间允许的最大间隔距离
     * **/
    public  Result phraseQuery(String key,List<Object> list,int  slop,QueryCondition  queryCondition){
        PhraseQuery  phraseQuery=new PhraseQuery();
        for(Object obj:list){
            phraseQuery.add(new Term(key,String.valueOf(obj)));
        }
        phraseQuery.setSlop(slop);
        return  executeQuery(phraseQuery,queryCondition);
    }

    /**
     * 相近词语搜索
     * <p>
     * FuzzyQuery是一种模糊查询，它可以简单地识别两个相近的词语。
     *
     * **/
    public  Result  fuzzyQuery(String key,String value,QueryCondition queryCondition){
        FuzzyQuery  fuzzyQuery=new FuzzyQuery(new Term(key,value));
        return executeQuery(fuzzyQuery,queryCondition);
    }

    /**
     * 通配符搜索
     * <p>
     * Lucene也提供了通配符的查询，这就是WildcardQuery。
     * 通配符“?”代表1个字符，而“*”则代表0至多个字符。
     *
     * */
     public  Result  wildcardQuery(String  key,String  wildcard,QueryCondition queryCondition){
         WildcardQuery  wildcardQuery=new WildcardQuery(new Term(key,wildcard));
         return  executeQuery(wildcardQuery,queryCondition);
     }

     /**
      * 范围查询
      * field 字段
      * start 开始位置
      * end  结束位置
      * iSBlow 是否含低
      * isCell 是否含顶
      * **/
     public  Result  numericRangeQuery(String  field,int start,int end ,boolean  isBlow,boolean  isCell ,QueryCondition queryCondition){
         Query  query= NumericRangeQuery.newIntRange(field,start,end,isBlow,isCell);
         return executeQuery(query,queryCondition);
     }


     /**
      * 分词查询
      * **/
    public  Result    analyzerParseQuery(String field,String  content ,QueryCondition queryCondition){
        IKAnalyzer  ikAnalyzer=new IKAnalyzer();
        QueryParser queryParser=new QueryParser(Version.LUCENE_CURRENT,field,ikAnalyzer);
        try {
            Query  query=queryParser.parse(content);
            return executeQuery(query,queryCondition);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Result.isFail();
    }

    /**
     * 多分词查询
     * **/
    public Result  multiFieldQueryParser(String[] fields, String content,QueryCondition queryCondition){
        IKAnalyzer  ikAnalyzer=new IKAnalyzer();
        MultiFieldQueryParser  multiFieldQueryParser=new MultiFieldQueryParser(Version.LUCENE_CURRENT,fields,ikAnalyzer);
        try {
            Query query=multiFieldQueryParser.parse(content);
            return executeQuery(query,queryCondition);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        closeReader();
        return Result.isOk();
    }

    /**
     * ik分词器查找数据
     * ikanlyzer
     * */
    public  void   IKAnalyzerQuery(String field,String content){
        IKAnalyzer  ikAnalyzer=new IKAnalyzer();
        analyzerQuery(ikAnalyzer,field,content);
    }


    /**
     * 其他分词器 TODO....
     * **/


    /**
     * 高亮查询
     * **/
    public  void  highLighter(String field,String content){
        IKAnalyzer  ikAnalyzer=new IKAnalyzer();
        QueryParser  queryParser=new QueryParser(Version.LUCENE_47,field,ikAnalyzer);
        try {
            Query query=queryParser.parse(content);
            TopDocs docs=indexSearcher.search(query, 100);
            // 关键字高亮显示的html标签，需要导入lucene-highlighter-xxx.jar
            SimpleHTMLFormatter  simpleHTMLFormatter=new SimpleHTMLFormatter("<span style='color:red'>", "</span>");
            Highlighter  highlighter=new Highlighter(simpleHTMLFormatter,new QueryScorer(query));
            for(ScoreDoc  doc:docs.scoreDocs){
                Document document=indexSearcher.doc(doc.doc);
                TokenStream tokenStream=ikAnalyzer.tokenStream(field,content);
                String result=highlighter.getBestFragment(tokenStream,document.get(field));
                System.out.println("高亮展示的结果:"+result);
            }
        } catch (ParseException | IOException | InvalidTokenOffsetsException e) {
            e.printStackTrace();
        }


    }

   /* *//** 直接打开索引
     * @request indexPath
     * **//*
    public  void  openResource(String  indexPath){
        try {
            fsDirectory= FSDirectory.open(new File(indexPath));
            directoryReader=DirectoryReader.open(fsDirectory);
            indexSearcher=new IndexSearcher(directoryReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    /**
     * 按照分类打开索引
     * @param category
     * **/
    public  void  openResource(String  indexPath,String category){
        File  businessFile=new File(indexPath+ File.separator+category);
        try {
            fsDirectory= FSDirectory.open(businessFile);
            directoryReader=DirectoryReader.open(fsDirectory);
            indexSearcher=new IndexSearcher(directoryReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public  void   openWriteResource(String  category){
        //String  indexPath=properties.getIndexPath();
        try {
            //File  businessFile=new File(indexPath+ File.separator+category);
            //fsDirectory= FSDirectory.open(businessFile);
            IKAnalyzer ikAnalyzer=new IKAnalyzer();
            IndexWriterConfig indexWriterConfig=new IndexWriterConfig(Version.LUCENE_CURRENT,ikAnalyzer);
            indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
            indexWriter =new IndexWriter(fsDirectory,indexWriterConfig);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /** 按照分类打开索引**/
    public  void   openResource(String category) {
        String  indexPath=properties.getIndexPath();
        File  businessFile=new File(indexPath+ File.separator+category);
        try {
            fsDirectory= FSDirectory.open(businessFile);
            directoryReader=DirectoryReader.open(fsDirectory);
            indexSearcher=new IndexSearcher(directoryReader);
        } catch (IOException e) {
            // e.printStackTrace();
        }
    }

    /** 按照分类打开索引**/
    public  Result   openResourceByDay(String category) {
        String  indexPath=properties.getIndexPath();
        File  businessFile=new File(indexPath+ File.separator+category);
        try {
            fsDirectory= FSDirectory.open(businessFile);
            directoryReader=DirectoryReader.open(fsDirectory);
            indexSearcher=new IndexSearcher(directoryReader);
        } catch (IOException e) {
            // e.printStackTrace();
            return  Result.isFail();
            //throw  new RuntimeException("12312312");
        }
        return  Result.isOk();
    }

    /** 多索引窗口查询 **/
    @SuppressWarnings("all")
    public Result    multiIndexQuery(QueryCondition queryParser) throws ParseException, IOException {
       String  root=properties.getIndexPath();
       List<DirectoryReader> list=new ArrayList<>();
        for(String category: SearchType.list()){
            try {
                list.add(DirectoryReader.open(FSDirectory.open(new File(root+File.separator+category))));
            } catch (IOException e) {
                continue;
            }
        }
        MultiReader  multiReader=new MultiReader(list.toArray(new DirectoryReader[list.size()]));
        IndexSearcher indexSearcher=new IndexSearcher(multiReader);
        String value= DateUtils.getDefaultDate();
        RangeParser parser=new RangeParser(Version.LUCENE_35,"data",new StandardAnalyzer(Version.LUCENE_35));
        Query  query=parser.parse(value);

        SortField  rank=new SortField("rank",SortField.Type.INT,false);
        SortField  like=new SortField("like",SortField.Type.INT,true);
        SortField  date =new SortField("date",SortField.Type.LONG,true);
        SortField  focus =new SortField("focus",SortField.Type.INT,true);
        SortField  browse =new SortField("browse",SortField.Type.INT,true);
        SortField  comment =new SortField("comment",SortField.Type.INT,true);
        Sort  sort=new Sort(rank,like,date,focus,browse,comment);

        TopFieldCollector collector =  TopFieldCollector.create(sort, 10000, true, true, false, false);
        //TopScoreDocCollector collector = TopScoreDocCollector.create(10000, false);
        TopDocs docs=null;
        if(queryParser.getPage()==null||queryParser.getPage()==0){
             docs= indexSearcher.search(query,10000,sort);
        }else {
            indexSearcher.search(query,collector);
            docs =collector.topDocs(queryParser.getPage(),queryParser.getPageSize());
        }
        System.out.println("size大小"+queryParser.getPage()+";pagesize"+queryParser.getPageSize());
        List<JSONObject> resList=new ArrayList<>();
        JSONObject obj=null;
         //topDocs(1);

        for(ScoreDoc doc:docs.scoreDocs){
            obj=new JSONObject();
            int  index=doc.doc;
            Document document=indexSearcher.doc(index);
            for(String key:LuceneFormatter.listFields()){
                /**转换一下date*/
                if(StringUtils.equals(key,"date")){
                    long   longDate=Long.parseLong(document.get(key));
                    obj.put(key,DateUtils.format(new Date(longDate)));
                }else {
                    obj.put(key,document.get(key));
                }
            }
            resList.add(obj);
        }
        if(multiReader!=null){
            multiReader.close();
        }
        System.out.println("docs.totalHits:"+docs.totalHits);
        return Result.isOk(resList,docs.totalHits);
    }

    /** 多索引窗口关键字查询 **/
    @SuppressWarnings("all")
    public Result    multiKeywordQuery(QueryCondition queryParser) throws ParseException, IOException {
        String  root=properties.getIndexPath();
        List<DirectoryReader> list=new ArrayList<>();
        for(String category: SearchType.list()){
            try {
                list.add(DirectoryReader.open(FSDirectory.open(new File(root+File.separator+category))));
            } catch (IOException e) {
                continue;
            }
        }
        MultiReader  multiReader=new MultiReader(list.toArray(new DirectoryReader[list.size()]));
        IndexSearcher indexSearcher=new IndexSearcher(multiReader);
        String value= DateUtils.getDefaultDate();
        /*RangeParser parser=new RangeParser(Version.LUCENE_35,"data",new StandardAnalyzer(Version.LUCENE_35));
        Query  query=parser.parse(queryParser.getKeyword());*/
        String [] fields=LuceneFormatter.listFields();
        IKAnalyzer ikAnalyzer=new IKAnalyzer();
        MultiFieldQueryParser multiFieldQueryParser=new MultiFieldQueryParser(Version.LUCENE_CURRENT,fields,ikAnalyzer);
        Query query=multiFieldQueryParser.parse(queryParser.getKeyword());

        SortField  rank=new SortField("rank",SortField.Type.INT,false);
        SortField  like=new SortField("like",SortField.Type.INT,true);
        SortField  date =new SortField("date",SortField.Type.LONG,true);
        SortField  focus =new SortField("focus",SortField.Type.INT,true);
        SortField  browse =new SortField("browse",SortField.Type.INT,true);
        SortField  comment =new SortField("comment",SortField.Type.INT,true);
        Sort  sort=new Sort(rank,like,date,focus,browse,comment);

        TopFieldCollector collector =  TopFieldCollector.create(sort, 10000, true, true, false, false);
        //TopScoreDocCollector collector = TopScoreDocCollector.create(10000, false);
        TopDocs docs=null;
        if(queryParser.getPage()==null||queryParser.getPage()==0){
            docs= indexSearcher.search(query,10000,sort);
        }else {
            indexSearcher.search(query,collector);
            docs =collector.topDocs(queryParser.getPage(),queryParser.getPageSize());
        }
        System.out.println("size大小"+queryParser.getPage()+";pagesize"+queryParser.getPageSize());
        List<JSONObject> resList=new ArrayList<>();
        JSONObject obj=null;
        //topDocs(1);

        for(ScoreDoc doc:docs.scoreDocs){
            obj=new JSONObject();
            int  index=doc.doc;
            Document document=indexSearcher.doc(index);
            for(String key:LuceneFormatter.listFields()){
                obj.put(key,document.get(key));
            }
            resList.add(obj);
        }
        if(multiReader!=null){
            multiReader.close();
        }
        System.out.println("docs.totalHits:"+docs.totalHits);
        return Result.isOk(resList,docs.totalHits);
    }


    /**  切换索引reader 使用reopen **/
    public  void  changeResource(String category){
            if(StringUtils.isEmpty(category)){
                return;
            }
            /** 改变reader .*/
            if(indexSearcher==null){
                openResource(category);
                return ;
            }
        try {
            IKAnalyzer ikAnalyzer=new IKAnalyzer();
            IndexWriterConfig indexWriterConfig=new IndexWriterConfig(Version.LUCENE_CURRENT,ikAnalyzer);
            indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
            IndexWriter indexWriter =new IndexWriter(fsDirectory,indexWriterConfig);
            IndexReader newReader = DirectoryReader.openIfChanged((DirectoryReader)directoryReader, indexWriter, false);//reader.reopen();      // 读入新增加的增量索引内容，满足实时索引需求
            if (newReader != null) {
                directoryReader.close();
                directoryReader = (DirectoryReader) newReader;
            }
            indexSearcher = new IndexSearcher(directoryReader);
        } catch (CorruptIndexException e) {
        } catch (IOException e) {
        }


    }



    public  void  closeReader(){
        if(directoryReader!=null){
            try {
                directoryReader.close();
            } catch (IOException e) {
                log.error("directoryReader close  is exception....");
                e.printStackTrace();
            }
        }
    }



}
