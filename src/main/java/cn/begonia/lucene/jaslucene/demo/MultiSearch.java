package cn.begonia.lucene.jaslucene.demo;

import cn.begonia.lucene.jaslucene.famatter.parser.RangeParser;
import cn.begonia.lucene.jaslucene.util.DateUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.IOException;

/**
 * @author begonia_chen
 * @data 2020/9/7 14:12
 * @description 多索引查询数据
 **/
public class MultiSearch {
    private static   String  root="D:\\data\\index";


    public  static  void  search() throws IOException, ParseException {
        Directory directory= FSDirectory.open(new File(root+File.separator+"cnblogs"));
        Directory directory1= FSDirectory.open(new File(root+File.separator+"hotspot"));
       // DirectoryReader dr=DirectoryReader.open(directory);
        ParallelCompositeReader reader=new ParallelCompositeReader();
        DirectoryReader  []  readers={DirectoryReader.open(directory),DirectoryReader.open(directory1)};
        MultiReader  multiReader=new MultiReader(readers);

        // ParallelAtomicReader
        //ParallelAtomicReader  compositeReader=new ParallelAtomicReader(AtomicReader.open(directory),AtomicReader.open(directory1));
        IndexSearcher indexSearcher=new IndexSearcher(multiReader);
        String  field="date";
        String  value="date:[2020-08-18 TO 2020-09-19]";
        RangeParser parser=new RangeParser(Version.LUCENE_35,field,new StandardAnalyzer(Version.LUCENE_35));
        Query  query=parser.parse(value);
        TopDocs docs= indexSearcher.search(query,10000);
        System.out.println("docs.size="+docs.scoreDocs.length);
        for(ScoreDoc doc:docs.scoreDocs){
           /* Explanation explanation=indexSearcher.explain(query,doc.doc);
            System.out.println(explanation.toString());*/
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
        //multiReader.close();
    }


    public static void main(String[] args) throws IOException, ParseException {
        long startTime=System.currentTimeMillis();
        search();
        System.out.println("总共耗时："+(System.currentTimeMillis()-startTime));

    }

}
