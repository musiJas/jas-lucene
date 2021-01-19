package cn.begonia.lucene.jaslucene.web;

import cn.begonia.lucene.jaslucene.common.Result;
import cn.begonia.lucene.jaslucene.common.SearchType;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author begonia_chen
 * @data 2020/8/17 11:31
 * @description
 **/
@RestController
public class HealthController {
    @Autowired
    RedisTemplate   redisTemplate;

    @RequestMapping("/test")
    public Object  showConsole(){
        redisTemplate.opsForValue().set("test","1231");
        Map<String,String> map=redisTemplate.opsForHash().entries("cnblogs");
        return JSONObject.toJSON(map);
    }


    @RequestMapping("/isOK")
    public  String  isOk(){
        return "isOk";
    }

    @RequestMapping("/category")
    public Result   category(){
        return Result.isOk(SearchType.listObject());
    }
}
