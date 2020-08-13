package cn.begonia.lucene.jaslucene.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
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
import org.springframework.stereotype.Service;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
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
    private IndexSearcher indexSearcher;

    /** 新增查询数据 **/
    public void  executeQuery(Query  query,int item){
        querySearch(query,item);
    }

    public  void  executeQuery(Query query){
        querySearch(query,100);
    }


    /**
     * 执行查询，并打印查询到的记录数
     * @param query
     * @throws IOException
     */
    public  void  querySearch(Query query,int total){
        try {
            TopDocs topDocs = indexSearcher.search(query, total);
            //打印查询到的记录数
            System.out.println("总共查询到" + topDocs.totalHits + "个文档");
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                //取得对应的文档对象
                Document document = indexSearcher.doc(scoreDoc.doc);
              /*  List<IndexableField> list= document.getFields();
                for(IndexableField field:list){
                    //System.out.println(field.name());
                }*/
                String fileName=document.get("fileName");
                System.out.println("fileName="+fileName);
                String fileSize=document.get("fileSize");
                System.out.println("fileSize="+fileSize);
                String filePath=document.get("filePath");
                System.out.println("filePath="+filePath);
                String  content=document.get("fileContent");
                System.out.println("content="+content);
                /*System.out.println("id：" + document.get("id"));
                System.out.println("title：" + document.get("title"));
                System.out.println("content：" + document.get("content"));*/
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    public  void  termQuery(String key,String value){
        TermQuery  query=new TermQuery(new Term(key,value));
        executeQuery(query);
    }

    /**
     * method
     * **/
    public   void  termQuery(Term  term){
        executeQuery(new TermQuery(term));
    }

    /***
     * * 多条件查询
     * <p>
     * BooleanQuery也是实际开发过程中经常使用的一种Query。
     * 它其实是一个组合的Query，在使用时可以把各种Query对象添加进去并标明它们之间的逻辑关系。
     * BooleanQuery本身来讲是一个布尔子句的容器，它提供了专门的API方法往其中添加子句，
     * 并标明它们之间的关系，以下代码为BooleanQuery提供的用于添加子句的API接口：
     */
    public  void   multipleQuery(Map<String,Object> map){
        List<Term> list=new ArrayList<>();
        for(Map.Entry<String,Object> ty:map.entrySet()){
            Term term=new Term(ty.getKey(),String.valueOf(ty.getValue()));
            list.add(term);
        }
        multipleQuery(list);
    }

    public  void   multipleQuery(List<Term> list){
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
        executeQuery(booleanQuery);
    }

    /**
     * 匹配前缀
     * <p>
     * PrefixQuery用于匹配其索引开始以指定的字符串的文档。就是文档中存在xxx%
     * <p>
     * **/
    public  void  prefixQuery(String key ,Object value){
        Query query=new PrefixQuery(new Term(key,String.valueOf(value)));
        executeQuery(query);
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
    public  void phraseQuery(String key,List<Object> list,int  slop){
        PhraseQuery  phraseQuery=new PhraseQuery();
        for(Object obj:list){
            phraseQuery.add(new Term(key,String.valueOf(obj)));
        }
        phraseQuery.setSlop(slop);
        executeQuery(phraseQuery);
    }

    /**
     * 相近词语搜索
     * <p>
     * FuzzyQuery是一种模糊查询，它可以简单地识别两个相近的词语。
     *
     * **/
    public  void  fuzzyQuery(String key,String value){
        FuzzyQuery  fuzzyQuery=new FuzzyQuery(new Term(key,value));
        executeQuery(fuzzyQuery);
    }

    /**
     * 通配符搜索
     * <p>
     * Lucene也提供了通配符的查询，这就是WildcardQuery。
     * 通配符“?”代表1个字符，而“*”则代表0至多个字符。
     *
     * */
     public  void  wildcardQuery(String  key,String  wildcard){
         WildcardQuery  wildcardQuery=new WildcardQuery(new Term(key,wildcard));
         executeQuery(wildcardQuery);
     }

     /**
      * 分词查询
      * **/
    public  void    analyzerParseQuery(String field,String  content){
        IKAnalyzer  ikAnalyzer=new IKAnalyzer();
        QueryParser  queryParser=new QueryParser(Version.LUCENE_CURRENT,field,ikAnalyzer);
        try {
            Query  query=queryParser.parse(content);
            executeQuery(query);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * 多分词查询
     * **/
    public   void   multiFieldQueryParser(String[] fields,String content){
        IKAnalyzer  ikAnalyzer=new IKAnalyzer();
        MultiFieldQueryParser  multiFieldQueryParser=new MultiFieldQueryParser(Version.LUCENE_CURRENT,fields,ikAnalyzer);
        try {
            Query query=multiFieldQueryParser.parse(content);
            executeQuery(query);
        } catch (ParseException e) {
            e.printStackTrace();
        }
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



    public  void  openResource(String  indexPath){
        try {
            fsDirectory= FSDirectory.open(new File(indexPath));
            directoryReader=DirectoryReader.open(fsDirectory);
            indexSearcher=new IndexSearcher(directoryReader);
        } catch (IOException e) {
            e.printStackTrace();
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
