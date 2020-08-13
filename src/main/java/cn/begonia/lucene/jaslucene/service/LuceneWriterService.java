package cn.begonia.lucene.jaslucene.service;

import cn.begonia.lucene.jaslucene.common.ResourceFactory;
import cn.begonia.lucene.jaslucene.resourece.ResourceAttribute;
import cn.begonia.lucene.jaslucene.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;

/**
 *  生成索引服务
 *
 * */

@Slf4j
@Service
public class LuceneWriterService {
    private FSDirectory  fsDirectory;
    private IndexWriter  indexWriter;

    @Autowired
    private ResourceFactory  resourceFactory;

    public  void  openResource(String  indexPath){
        try {
            fsDirectory= FSDirectory.open(new File(indexPath));
            IKAnalyzer ikAnalyzer=new IKAnalyzer();
            IndexWriterConfig indexWriterConfig=new IndexWriterConfig(Version.LUCENE_CURRENT,ikAnalyzer);
            indexWriter =new IndexWriter(fsDirectory,indexWriterConfig);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  void  createIndex(ResourceAttribute  attribute){
        long  startTime=System.currentTimeMillis();
        log.info("开始创建索引数据.");
        DocumentConvert documentConvert=resourceFactory.get(attribute);
        openResource(attribute.getIndexPath());
      /*  *//** 清空索引文件*//*
        FileUtil.deleteFiles(file);*/
        attribute.setWriter(indexWriter);
        documentConvert.convertHandlerDocument(attribute);
        closeWriter();
        long  endTime=System.currentTimeMillis();
        log.info("总共耗时:"+(endTime-startTime));
    }

    public  void  closeWriter(){
        if(indexWriter!=null){
            try {
                indexWriter.close();
            } catch (IOException e) {
                log.error("indexWriter close  is exception....");
                e.printStackTrace();
            }
        }
    }
}
