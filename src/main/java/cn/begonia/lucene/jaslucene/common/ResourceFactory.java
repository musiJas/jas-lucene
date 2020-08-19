package cn.begonia.lucene.jaslucene.common;

import cn.begonia.lucene.jaslucene.resourece.ResourceAttribute;
import cn.begonia.lucene.jaslucene.service.handler.DocumentConvert;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author begonia_chen
 * @data 2020/8/13 15:29
 * @description
 **/
@Service
public class ResourceFactory {

    private  final ApplicationContext  applicationContext;
    public   ResourceFactory(ApplicationContext applicationContext){
        this.applicationContext=applicationContext;
    }

    public DocumentConvert   get(ResourceAttribute attribute){
        Map<String, DocumentConvert> map=applicationContext.getBeansOfType(DocumentConvert.class);
        return  map.get(attribute.getType().getInstanceName());
    }

}
