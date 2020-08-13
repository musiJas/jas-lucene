package cn.begonia.lucene.jaslucene.web;

import cn.begonia.lucene.jaslucene.common.ResourceType;
import cn.begonia.lucene.jaslucene.resourece.FileResource;
import cn.begonia.lucene.jaslucene.service.LuceneDocumentService;
import cn.begonia.lucene.jaslucene.service.LuceneReaderService;
import cn.begonia.lucene.jaslucene.service.LuceneWriterService;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    String  index="D:\\data\\index";

    @RequestMapping("/createIndex")
    public JSONObject  createIndex(){
        FileResource  resource=new FileResource();
        resource.setType(ResourceType.file);
        resource.setIndexPath(index);
        resource.setResourcePath(resourcePath);
        luceneWriterService.createIndex(resource);
        return  null;
    }

    @RequestMapping("/query")
    public JSONObject  queryObject(String field,String content){
        long  startTime=System.currentTimeMillis();
        FileResource  resource=new FileResource();
        resource.setIndexPath(index);
        luceneReaderService.openResource(index);
        luceneReaderService.termQuery(field,content);
        luceneReaderService.closeReader();
        long  endTime=System.currentTimeMillis();
        log.info("总共耗时:"+(endTime-startTime));
        return null;
    }

}
