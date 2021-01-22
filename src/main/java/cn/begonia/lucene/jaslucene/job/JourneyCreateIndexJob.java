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

@Slf4j
@Component
@PropertySource("classpath:application.properties")
public class JourneyCreateIndexJob {

    @Autowired
    ContextProperties contextProperties;
    @Autowired
    LuceneWriterService luceneWriterService;


    //@Scheduled(cron=" 0 15 10 ? * SUN-SAT")
    //@Scheduled(cron = "${jobs.schedule}")
    //@Scheduled(fixedRate = 3000)
    public void startCreateIndex() {
        log.info("开始创建索引数据.{}" + CacheType.journey.getKey());
        RedisSource redisSource = new RedisSource();
        redisSource.setCategory(CacheType.journey.getKey());
        redisSource.setIndexPath(contextProperties.getIndexPath() + File.separator + CacheType.journey.getKey());
        redisSource.setType(ResourceType.redis);
        luceneWriterService.createIndex(redisSource);
        log.info("结束创建索引数据.");
    }


}
