package cn.begonia.lucene.jaslucene.config;

import cn.begonia.lucene.jaslucene.common.CacheType;
import cn.begonia.lucene.jaslucene.resourece.ResourceAttribute;
import cn.begonia.lucene.jaslucene.service.handler.LuceneReaderService;
import cn.begonia.lucene.jaslucene.service.handler.LuceneWriterService;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

@Component
public class ServerDestroy implements DisposableBean {
    @Override
    public void destroy() throws Exception {
        /** 释放文件引用... 主要是索引写锁，***/
        LuceneReaderService luceneReaderService= ServerStartup.getApplicationContext().getBean(LuceneReaderService.class);
        LuceneWriterService luceneWriterService= ServerStartup.getApplicationContext().getBean(LuceneWriterService.class);
        for(CacheType type:CacheType.values()){
            luceneReaderService.closeReader(type.getKey());
            luceneWriterService.closeWriter(ResourceAttribute.builder().category(type.getKey()).build());
        }

    }
}
