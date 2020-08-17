package cn.begonia.lucene.jaslucene.config;

import cn.begonia.lucene.jaslucene.util.CacheUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author begonia_chen
 * @data 2020/8/17 10:45
 * @description
 **/

@Configuration
@Import({RedisConfig.class, CacheUtils.class})
public class RedisAutoConfiguration {


}
