package cn.begonia.lucene.jaslucene.job;

import cn.begonia.lucene.jaslucene.common.CacheType;
import cn.begonia.lucene.jaslucene.common.ResourceType;
import cn.begonia.lucene.jaslucene.config.ContextProperties;
import cn.begonia.lucene.jaslucene.resourece.RedisSource;
import cn.begonia.lucene.jaslucene.service.handler.LuceneWriterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @author begonia_chen
 * @data 2020/8/17 11:10
 * @description redis生成索引的方法
 **/
@Slf4j
@Component
@PropertySource("classpath:application.properties")
public class CnblogsCreateIndexJob {

    @Autowired
    ContextProperties   contextProperties;
    @Autowired
    LuceneWriterService  luceneWriterService;


    //@Scheduled(cron=" 0 15 10 ? * SUN-SAT")
    @Scheduled(cron = "${jobs.schedule}")
    public  void  startCreateIndex(){
        log.info("开始创建索引数据.{}"+CacheType.cnblogs.getKey());
        RedisSource  redisSource=new RedisSource();
        redisSource.setKey(CacheType.cnblogs.getKey());
        redisSource.setIndexPath(contextProperties.getIndexPath()+ File.separator+CacheType.cnblogs.getKey());
        redisSource.setType(ResourceType.redis);
        luceneWriterService.createIndex(redisSource);
        log.info("结束创建索引数据.");
    }

}
