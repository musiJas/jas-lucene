package cn.begonia.lucene.jaslucene.job;

import cn.begonia.lucene.jaslucene.common.ResourceFactory;
import cn.begonia.lucene.jaslucene.common.ResourceType;
import cn.begonia.lucene.jaslucene.config.ContextProperties;
import cn.begonia.lucene.jaslucene.resourece.RedisSource;
import cn.begonia.lucene.jaslucene.service.DocumentConvert;
import cn.begonia.lucene.jaslucene.service.LuceneWriterService;
import cn.begonia.lucene.jaslucene.service.impl.RedisConvertServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author begonia_chen
 * @data 2020/8/17 11:10
 * @description redis生成索引的方法
 **/
@Slf4j
@Component
public class RedisCreateIndexJob {

    @Autowired
    ContextProperties   contextProperties;
    @Autowired
    LuceneWriterService  luceneWriterService;


    //@Scheduled(cron="0 0/1 * * * ?")
    public  void  startCreateIndex(){
        log.info("开始创建索引数据.");
        String  executeKey="cnblogs";
        RedisSource  redisSource=new RedisSource();
        redisSource.setKey(executeKey);
        redisSource.setIndexPath(contextProperties.getIndexPath());
        redisSource.setType(ResourceType.redis);
        luceneWriterService.createIndex(redisSource);
        log.info("结束创建索引数据.");
    }

}
