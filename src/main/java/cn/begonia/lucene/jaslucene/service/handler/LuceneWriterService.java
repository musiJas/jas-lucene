package cn.begonia.lucene.jaslucene.service.handler;

import cn.begonia.lucene.jaslucene.common.ResourceFactory;
import cn.begonia.lucene.jaslucene.resourece.ResourceAttribute;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wltea.analyzer.lucene.IKAnalyzer;

import javax.smartcardio.ATR;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *  生成索引服务
 *
 * */

@Slf4j
@Service
public class LuceneWriterService {
    private   static   Map<String,IndexWriter>  indexWriterMap=new ConcurrentHashMap<>(16); // 存放indexWriter 并发问题

    @Autowired
    private ResourceFactory  resourceFactory;

    private  static  final  Object obj=new Object();


    /** 初始化时会调用一次 **/
    @SuppressWarnings("all")
    public synchronized   void   openResource(ResourceAttribute  attribute){
        try {
            FSDirectory fsDirectory= FSDirectory.open(new File(attribute.getIndexPath()));
            IKAnalyzer ikAnalyzer=new IKAnalyzer();
            IndexWriterConfig indexWriterConfig=new IndexWriterConfig(Version.LUCENE_CURRENT,ikAnalyzer);
            indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
            IndexWriter  indexWriter =new IndexWriter(fsDirectory,indexWriterConfig);
            indexWriterMap.put(attribute.getCategory(),indexWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  void  createIndex(ResourceAttribute  attribute){
        long  startTime=System.currentTimeMillis();
        log.info("开始创建索引数据.");
        DocumentConvert documentConvert=resourceFactory.get(attribute);
        if(indexWriterMap.get(attribute.getCategory())!=null){
            attribute.setWriter(indexWriterMap.get(attribute.getCategory()));
        }else {
            openResource(attribute);
            attribute.setWriter(indexWriterMap.get(attribute.getCategory()));
        }
        /*  *//** 清空索引文件*//*
        FileUtil.deleteFiles(file);*/
        documentConvert.convertHandlerDocument(attribute);
        closeWriter(attribute);
        long  endTime=System.currentTimeMillis();
        log.info("总共耗时:"+(endTime-startTime));
    }


    public  void  closeWriter(ResourceAttribute attribute){
        IndexWriter  indexWriter=indexWriterMap.get(attribute.getCategory());
        indexWriterMap.remove(attribute.getCategory()); // 清除掉以便重新打开
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
