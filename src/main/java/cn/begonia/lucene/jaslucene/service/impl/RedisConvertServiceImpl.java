package cn.begonia.lucene.jaslucene.service.impl;

import cn.begonia.lucene.jaslucene.resourece.RedisSource;
import cn.begonia.lucene.jaslucene.resourece.ResourceAttribute;
import cn.begonia.lucene.jaslucene.service.DocumentConvert;
import cn.begonia.lucene.jaslucene.util.CacheUtils;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

/**
 * @author begonia_chen
 * @data 2020/8/14 10:14
 * @description 从缓存中读取数据用于生成索引
 **/
@Slf4j
@Service
public class RedisConvertServiceImpl implements DocumentConvert  {

    @Autowired
    CacheUtils  cacheUtils;

    @Override
    public void convertHandlerDocument(ResourceAttribute attribute) {
        IndexWriter indexWriter = attribute.getWriter();
        RedisSource  rs= (RedisSource) attribute;
        String keys=rs.getKey();
        if(StringUtils.isNotEmpty(keys)){
            Map<Object, Object>  map=cacheUtils.hgetAll(keys);
            createIndex(indexWriter,map);
        }
    }

    public  void   createIndex(IndexWriter indexWriter,Map<Object,Object> map)   {
        Document doc=null;
        for(Map.Entry<Object,Object> ty:map.entrySet()){
            JSONObject obj=JSONObject.parseObject(String.valueOf(ty.getValue()));
            for(Map.Entry<String, Object> ob:obj.entrySet()){
                doc=new Document();
                Field field = new StringField(ob.getKey(), String.valueOf(ob.getValue()), Field.Store.YES);
                doc.add(field);
            }
            try {
                indexWriter.addDocument(doc);
            } catch (IOException e) {
                log.debug("key "+ty.getKey()+"建立索引失败...");
            }
        }
        try {
            indexWriter.commit();
            indexWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
