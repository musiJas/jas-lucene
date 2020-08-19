package cn.begonia.lucene.jaslucene.service.handler;

import cn.begonia.lucene.jaslucene.resourece.ResourceAttribute;
import org.apache.lucene.document.Document;


/**
 * @author begonia_chen
 * @data 2020/8/13 15:31
 * @description
 **/
public interface DocumentConvert {

    void  convertHandlerDocument(ResourceAttribute attribute);
}
