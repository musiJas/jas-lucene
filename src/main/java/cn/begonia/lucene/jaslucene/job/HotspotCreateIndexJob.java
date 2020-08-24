package cn.begonia.lucene.jaslucene.job;

import cn.begonia.lucene.jaslucene.common.CacheType;
import cn.begonia.lucene.jaslucene.common.ResourceType;
import cn.begonia.lucene.jaslucene.config.ContextProperties;
import cn.begonia.lucene.jaslucene.resourece.RedisSource;
import cn.begonia.lucene.jaslucene.service.handler.LuceneWriterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @author begonia_chen
 * @data 2020/8/19 10:26
 * @description
 **/
@Slf4j
@Component
public class HotspotCreateIndexJob {

    @Autowired
    ContextProperties contextProperties;
    @Autowired
    LuceneWriterService luceneWriterService;


    @Scheduled(cron="0 0/1 * * * ?")
    public  void  startCreateIndex(){
        log.info("开始创建索引数据.");
        RedisSource redisSource=new RedisSource();
        redisSource.setKey(CacheType.hotspot.getKey());
        redisSource.setIndexPath(contextProperties.getIndexPath()+ File.separator+CacheType.hotspot.getKey());
        redisSource.setType(ResourceType.redis);
        luceneWriterService.createIndex(redisSource);
        log.info("结束创建索引数据.");
    }

}