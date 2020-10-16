/*
package cn.begonia.lucene.jaslucene;

import cn.begonia.lucene.jaslucene.util.CacheUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import java.util.Map;

@WebAppConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest
public  class JasLuceneApplicationTests {

    @Autowired
    CacheUtils  cacheUtils;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Test
    public void contextLoads() {
        String  key="cnblogs";

       Map<Object, Object>  map=cacheUtils.hgetAll(key);
        System.out.println(map.size());

    }

}
*/
