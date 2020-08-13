package cn.begonia.lucene.jaslucene.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;
import org.springframework.stereotype.Service;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
/***
 * 更新删除 索引服务
 * @author  begonia_chen
 * */
@Slf4j
@Service
public class LuceneDocumentService {

    public   void   deleteDocument(Directory directory, Term  term){
        //Analyzer analyzer = new StandardAnalyzer(); // 标准分词器，适用于英文
        //Analyzer analyzer = new SmartChineseAnalyzer();//中文分词
        //Analyzer analyzer = new ComplexAnalyzer();//中文分词
        //Analyzer analyzer = new IKAnalyzer();//中文分词
        Analyzer analyzer = new IKAnalyzer();//中文分词
        //创建索引写入配置
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_47,analyzer);
        //创建索引写入对象
        try {
            IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);
            indexWriter.deleteDocuments(term);
            indexWriter.commit();
            indexWriter.close();
            System.out.println("删除完成!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param doc  新的文档内容
     * @term  term  更新的条件
     * */
    public   void  updateDocument(Directory directory, Document  doc,Term  term){
        Analyzer analyzer = new IKAnalyzer();//中文分词
        //创建索引写入配置
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_47,analyzer);
        try {
            IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);
            indexWriter.updateDocument(term,doc);
            indexWriter.commit();
            indexWriter.close();
            System.out.println("更新完成!");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



}
