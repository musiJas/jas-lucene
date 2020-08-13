package cn.begonia.lucene.jaslucene.demo;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.*;

public class demo2 {
    public static void main(String[] args) throws FileNotFoundException {
        String  resource="D:\\data\\text";
        String  index="D:\\data\\index";
        String  key="差矣";
        long  startTime= System.currentTimeMillis();
        queryIndex(index,key);

      /*  try {
            createIndex(index,resource);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        long  endTime= System.currentTimeMillis();
        System.out.println("总共耗时："+(endTime-startTime));
        //System.out.println(getStringByFile(new File(resource)));
    }

    @SuppressWarnings("all")
    public  static  String  queryIndex(String  indexPath,String key){
        try {
            Directory directory= FSDirectory.open(new File(indexPath));
            DirectoryReader dr=DirectoryReader.open(directory);
            IndexSearcher indexSearcher=new IndexSearcher(dr);
            IKAnalyzer  ik=new IKAnalyzer();
            QueryParser  qp=new QueryParser(Version.LUCENE_47,"fileContent",ik);

            //Query query=new TermQuery(new Term("fileContent",key));
            Query  query=qp.parse(key);
            TopDocs docs= indexSearcher.search(query,10);
            System.out.println("docs.size="+docs.scoreDocs.length);
            for(ScoreDoc doc:docs.scoreDocs){
                int  index=doc.doc;
                Document document=indexSearcher.doc(index);
                String fileName=document.get("fileName");
                System.out.println("fileName="+fileName);
                String fileSize=document.get("fileSize");
                System.out.println("fileSize="+fileSize);
                String filePath=document.get("filePath");
                System.out.println("filePath="+filePath);
                String  content=document.get("fileContent");
                System.out.println("content="+content);
            }
            dr.close();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        System.out.println("查找完成");
        return "";
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
}
