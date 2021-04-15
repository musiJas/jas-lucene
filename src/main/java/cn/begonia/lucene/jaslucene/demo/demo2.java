package cn.begonia.lucene.jaslucene.demo;

import cn.begonia.lucene.jaslucene.common.CacheType;
import cn.begonia.lucene.jaslucene.famatter.LuceneFormatter;
import cn.begonia.lucene.jaslucene.famatter.parser.RangeParser;
import cn.begonia.lucene.jaslucene.util.DateUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.ar.ArabicAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.*;


@SuppressWarnings("all")
public class demo2 {

    private  static  String  indexStaticPath="D:\\data\\index\\movie";


    private  static  String  indexStaticRoot="D:\\data\\index\\";

    public static void main(String[] args) throws FileNotFoundException, ParseException, java.text.ParseException {
        String  resource="D:\\data\\text";
        String  index="D:\\data\\index\\reading";
        String  key="date";
        String  value="date:[2021-01-01 TO 2021-01-19]";
        //String  value="like:[10 TO 100]";

        long  startTime= System.currentTimeMillis();
        String contentTitle="title";
        String  content="特工";
        //String  content="title:codermy";
        String [] multi={"title","content","auth","focus"};
        multiFieldQueryParser(multi,content);
        //queryStringResult(contentTitle,content);
        //queryIndex(index,"拳头");
        // queryNumericResult(key,value);
        /*try {
            createIndex(index,resource);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        long  endTime= System.currentTimeMillis();
        System.out.println("总共耗时："+(endTime-startTime));
    }

    /****/

    /**
     * 多分词查询
     * **/
    public  static void   multiFieldQueryParser(String[] fields,String content){
        IKAnalyzer  ikAnalyzer=new IKAnalyzer();
        SimpleAnalyzer simpleAnalyzer=new SimpleAnalyzer(Version.LUCENE_CURRENT);
        ArabicAnalyzer arabicAnalyzer=new ArabicAnalyzer(Version.LUCENE_47);
        MultiFieldQueryParser multiFieldQueryParser=new MultiFieldQueryParser(Version.LUCENE_CURRENT,fields,ikAnalyzer);
        try {
            Query query=multiFieldQueryParser.parse(content);
            //executeQuery(query);
            executePageQuery(query);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    public static  void  queryStringResult(String field,String value) throws ParseException {
        QueryParser  parser=new QueryParser(Version.LUCENE_35,field,new StandardAnalyzer(Version.LUCENE_35));
        Query  query=parser.parse(value);
        executeQuery(query);
    }

    public  static  String  queryNumericResult(String field,String value) throws ParseException, java.text.ParseException {
        RangeParser parser=new RangeParser(Version.LUCENE_47,field,new IKAnalyzer());
        Query  query=parser.parse(value);
        executeQuery(query);
        return "";
    }

    @SuppressWarnings("all")
    public  static  void  executePageQuery(Query query){
        try {
            Directory directory= FSDirectory.open(new File(indexStaticPath));
            DirectoryReader dr=DirectoryReader.open(directory);
            IndexSearcher indexSearcher=new IndexSearcher(dr);

            Sort sort = LuceneFormatter.getDefaultSort(CacheType.movie.getKey());
            TopFieldCollector collector = TopFieldCollector.create(sort, 10000, true, true, false, false);

            indexSearcher.search(query, collector);
            TopDocs docs = collector.topDocs(0, 30);

           // TopDocs docs= indexSearcher.search(query,10000);
            System.out.println("docs.size="+docs.scoreDocs.length);
            for(ScoreDoc doc:docs.scoreDocs){
                Explanation  explanation=indexSearcher.explain(query,doc.doc);
                System.out.println(explanation.toString());
                int  index=doc.doc;
                Document document=indexSearcher.doc(index);
                String titles=document.get("title");
                System.out.println("title="+titles);
                String urls=document.get("url");
                System.out.println("url="+urls);
                String auths=document.get("auth");
                System.out.println("auth="+auths);
                String dates=document.get("date");
                System.out.println("date="+ DateUtils.parseTimeMillis(dates));
                String  digs=document.get("like");
                System.out.println("dig="+digs);
                String  comments=document.get("comment");
                System.out.println("comment="+comments);
                String  browse=document.get("browse");
                System.out.println("browse="+browse);
                String  content=document.get("content");
                System.out.println("content="+content);
                String  img=document.get("img");
                System.out.println("img="+img);
            }
            dr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("查找完成");



    }



    public static  void  executeQuery(Query query){
        try {
            Directory directory= FSDirectory.open(new File(indexStaticPath));
            DirectoryReader dr=DirectoryReader.open(directory);
            IndexSearcher indexSearcher=new IndexSearcher(dr);
       /*     IKAnalyzer  ik=new IKAnalyzer();
            ArabicAnalyzer   arabicAnalyzer=new ArabicAnalyzer(Version.LUCENE_CURRENT);
            BooleanQuery  bq=new BooleanQuery();
            QueryParser  qp=new QueryParser(Version.LUCENE_CURRENT,"title",new SimpleAnalyzer(Version.LUCENE_CURRENT));
            Query  query1=new TermQuery(new Term("titles",key));
            Query  query=qp.parse(key);
            bq.add(query, BooleanClause.Occur.MUST);
            bq.add(query1, BooleanClause.Occur.MUST);*/

            TopDocs docs= indexSearcher.search(query,10000);
            System.out.println("docs.size="+docs.scoreDocs.length);
            for(ScoreDoc doc:docs.scoreDocs){
                Explanation  explanation=indexSearcher.explain(query,doc.doc);
                System.out.println(explanation.toString());
                int  index=doc.doc;
                Document document=indexSearcher.doc(index);
                String titles=document.get("title");
                System.out.println("title="+titles);
                String urls=document.get("url");
                System.out.println("url="+urls);
                String auths=document.get("auth");
                System.out.println("auth="+auths);
                String dates=document.get("date");
                System.out.println("date="+ DateUtils.parseTimeMillis(dates));
                String  digs=document.get("like");
                System.out.println("dig="+digs);
                String  comments=document.get("comment");
                System.out.println("comment="+comments);
                String  browse=document.get("browse");
                System.out.println("browse="+browse);
                String  content=document.get("content");
                System.out.println("content="+content);
                String  img=document.get("img");
                System.out.println("img="+img);
            }
            dr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("查找完成");
    }


    @SuppressWarnings("all")
    public  static  String  queryIndex(String  indexPath,String key){
        try {
            Directory directory= FSDirectory.open(new File(indexPath));
            DirectoryReader dr=DirectoryReader.open(directory);
            IndexSearcher indexSearcher=new IndexSearcher(dr);
            IKAnalyzer  ik=new IKAnalyzer();

            ArabicAnalyzer   arabicAnalyzer=new ArabicAnalyzer(Version.LUCENE_CURRENT);
            BooleanQuery  bq=new BooleanQuery();
            QueryParser  qp=new QueryParser(Version.LUCENE_CURRENT,"title",new SimpleAnalyzer(Version.LUCENE_CURRENT));


            Query  query1=new TermQuery(new Term("titles",key));
            Query  query=qp.parse(key);
            bq.add(query, BooleanClause.Occur.MUST);
            bq.add(query1, BooleanClause.Occur.MUST);
            TopDocs docs= indexSearcher.search(query,10);
            System.out.println("docs.size="+docs.scoreDocs.length);


            for(ScoreDoc doc:docs.scoreDocs){
                Explanation  explanation=indexSearcher.explain(query,doc.doc);
                System.out.println(explanation.toString());
                int  index=doc.doc;
                Document document=indexSearcher.doc(index);


                String titles=document.get("title");
                String  highlight= displayHtmlHighlight(bq,ik,"title",titles,200);
                System.out.println("highlight=>"+highlight);


                System.out.println("title="+titles);
                String detail=document.get("detail");
                System.out.println("detail="+detail);
                String  details= displayHtmlHighlight(bq,ik,"detail",detail,200);
                System.out.println("highlight=>"+details);

                String urls=document.get("url");
                System.out.println("url="+urls);
                String auths=document.get("auth");
                System.out.println("auth="+auths);
                String dates=document.get("date");
                System.out.println("date="+dates);
                String  digs=document.get("like");
                System.out.println("dig="+digs);
                String  comments=document.get("comment");
                System.out.println("comment="+comments);
                String  browse=document.get("browse");
                System.out.println("browse="+browse);
                String  content=document.get("content");
                System.out.println("content="+content);
                String  img=document.get("img");
                System.out.println("img="+img);
            }
            dr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("查找完成");
        return "";
    }



    /**
     * 获取高亮显示结果的html代码
     * @param query 查询
     * @param analyzer 分词器
     * @param fieldName 域名
     * @param fieldContent 域内容
     * @param fragmentSize 结果的长度（不含html标签长度）
     * @return 结果（一段html代码）
     * @throws IOException
     */
    static String displayHtmlHighlight(Query query, Analyzer analyzer, String fieldName, String fieldContent, int fragmentSize) throws IOException, InvalidTokenOffsetsException
    {
        //创建一个高亮器
        Highlighter highlighter = new  Highlighter(new SimpleHTMLFormatter("<font color='red'>", "</font>"), new QueryScorer(query));
        Fragmenter fragmenter = new SimpleFragmenter(fragmentSize);
        highlighter.setTextFragmenter(fragmenter);
        return highlighter.getBestFragment(analyzer, fieldName, fieldContent);
    }


    public   static  void  createIndex(String indexPath,String  resourcePath) throws IOException {
        Directory   dir=FSDirectory.open(new File(indexPath));
        IKAnalyzer ikAnalyzer=new IKAnalyzer();
        //Analyzer ikAnalyzer = new StandardAnalyzer();
        IndexWriterConfig writerConfig=new IndexWriterConfig(Version.LUCENE_CURRENT,ikAnalyzer);
        IndexWriter writer=new IndexWriter(dir,writerConfig);
        File  file=new File(resourcePath);
        if(file.isDirectory()){
            System.out.println("listFiles="+file.listFiles().length);
            Document  doc=null;
            for(File fl:file.listFiles()){
                doc=new Document();
                String  fileName=fl.getName();
                Field fieldName=new TextField("fileName",fileName, Field.Store.YES);
                long fileSize=fl.length();
                Field  fieldSize=new TextField("fileSize",String.valueOf(fileSize),Field.Store.YES);
                String  filePath=fl.getPath();
                Field  fieldPath=new StoredField("filePath",filePath);
                String fileContent=getStringByFile(fl);
                Field  fieldContent=new TextField("fileContent",fileContent,Field.Store.YES);
                doc.add(fieldName);
                doc.add(fieldSize);
                doc.add(fieldPath);
                doc.add(fieldContent);
                writer.addDocument(doc);
            }

        }
        writer.commit();
        writer.close();
        System.out.println("创建完成!");
    }



    public static  String  getStringByFile(File file) throws FileNotFoundException {
        BufferedReader bf=new BufferedReader(new FileReader(file));
        StringBuffer sb=new StringBuffer();
        String temp="";
        try {
            while((temp=bf.readLine())!=null){
                sb.append(temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }


  /*  *//**  切换索引reader 使用reopen **//*
    @SuppressWarnings("all")
    public  void  changeResource(String category) throws IOException, ParseException {
        Directory directory= FSDirectory.open(new File(indexStaticRoot+category));
        DirectoryReader dr=DirectoryReader.open(directory);
        IndexSearcher indexSearcher=new IndexSearcher(dr);

        queryStringResult("title","title:Urule开源版");
        try {
            IKAnalyzer  ikAnalyzer=new IKAnalyzer();
            IndexWriterConfig indexWriterConfig=new IndexWriterConfig(Version.LUCENE_CURRENT,ikAnalyzer);
            indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
            IndexWriter indexWriter =new IndexWriter(directory,indexWriterConfig);
            IndexReader newReader = DirectoryReader.openIfChanged(dr, indexWriter, false);//reader.reopen();      // 读入新增加的增量索引内容，满足实时索引需求
            if (newReader != null) {
                dr.close();
                dr = (DirectoryReader) newReader;
            }
            indexSearcher = new IndexSearcher(dr);
            String  value="date:[2020-08-18 TO 2020-08-19]";
             queryNumericResult("date",value);
        } catch (CorruptIndexException e) {
        } catch (IOException e) {
        }*/


    //}


}
