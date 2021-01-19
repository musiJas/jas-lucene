package cn.begonia.lucene.jaslucene.web;

import cn.begonia.lucene.jaslucene.common.CacheType;
import cn.begonia.lucene.jaslucene.common.QueryCondition;
import cn.begonia.lucene.jaslucene.common.ResourceType;
import cn.begonia.lucene.jaslucene.config.ContextProperties;
import cn.begonia.lucene.jaslucene.resourece.FileResource;
import cn.begonia.lucene.jaslucene.service.handler.LuceneReaderService;
import cn.begonia.lucene.jaslucene.service.handler.LuceneWriterService;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

/**
 * @author begonia_chen
 * @data 2020/8/13 15:54
 * @description
 **/
@Slf4j
@RestController
public class FileCreateController {
    @Autowired
    private LuceneWriterService   luceneWriterService;
    @Autowired
    private LuceneReaderService   luceneReaderService;

    String  resourcePath="D:\\data\\text";
    //String  index="D:\\data\\index";

    @Autowired
    ContextProperties  properties;

    @RequestMapping("/createIndex")
    public JSONObject  createIndex(){
        FileResource  resource=new FileResource();
        resource.setType(ResourceType.file);
        resource.setIndexPath(properties.getIndexPath());
        resource.setResourcePath(resourcePath);
        luceneWriterService.createIndex(resource);
        return  null;
    }

    @RequestMapping("/query")
    public JSONObject  queryObject(String field,String content){
        long  startTime=System.currentTimeMillis();
       /* FileResource  resource=new FileResource();
        resource.setIndexPath(properties.getIndexPath());*/
        String  cacheType= CacheType.cnblogs.getKey();
        luceneReaderService.openResource(properties.getIndexPath()+ File.separator+cacheType);
        QueryCondition   queryCondition=new QueryCondition();
        luceneReaderService.termQuery(field,content,queryCondition);
        luceneReaderService.closeReader();
        long  endTime=System.currentTimeMillis();
        log.info("总共耗时:"+(endTime-startTime));
        return null;
    }

}
