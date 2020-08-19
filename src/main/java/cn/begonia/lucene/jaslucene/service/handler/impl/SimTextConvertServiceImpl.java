package cn.begonia.lucene.jaslucene.service.handler.impl;

import cn.begonia.lucene.jaslucene.resourece.ResourceAttribute;
import cn.begonia.lucene.jaslucene.resourece.SimtextResource;
import cn.begonia.lucene.jaslucene.service.handler.DocumentConvert;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author begonia_chen
 * @data 2020/8/13 15:39
 * @description
 **/
@Service
public class SimTextConvertServiceImpl implements DocumentConvert {

    @Override
    public void convertHandlerDocument(ResourceAttribute attribute) {
        SimtextResource   resource= (SimtextResource) attribute;
        Document doc=null;
        IndexWriter  writer=attribute.getWriter();
        if(resource.isMultiResult()){
            List<Map<String,Object>> list=resource.getList();
            for(Map<String,Object> map:list){
                /** 将复合文本写入索引库中**/
                doc= new Document();
                 for(Map.Entry<String,Object> ty:map.entrySet()){
                     Field field=new TextField(ty.getKey(),String.valueOf(ty.getValue()), Field.Store.YES);
                     doc.add(field);
                 }
            }
            try {
                writer.addDocument(doc);
            } catch (IOException e) {
                e.printStackTrace();
            }
            resource.closeHandler(writer);
        }else {
            Field  field=new TextField(resource.getField(),resource.getContent(), Field.Store.YES);
            doc= new Document();
            doc.add(field);
            try {
                writer.addDocument(doc);
            } catch (IOException e) {
                e.printStackTrace();
            }
            resource.closeHandler(writer);
        }
    }
}
