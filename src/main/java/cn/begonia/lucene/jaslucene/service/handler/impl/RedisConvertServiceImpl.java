package cn.begonia.lucene.jaslucene.service.handler.impl;

import cn.begonia.lucene.jaslucene.famatter.LuceneFormatter;
import cn.begonia.lucene.jaslucene.resourece.RedisSource;
import cn.begonia.lucene.jaslucene.resourece.ResourceAttribute;
import cn.begonia.lucene.jaslucene.service.handler.DocumentConvert;
import cn.begonia.lucene.jaslucene.util.CacheUtils;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.stereotype.Service;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.util.HashMap;
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

    public  void   createIndex(IndexWriter indexWriter,Map<Object,Object> map,String category)   {
        Document doc=null;
        for(Map.Entry<Object,Object> ty:map.entrySet()){
            String key=String.valueOf(ty.getKey());
            JSONObject obj=JSONObject.parseObject(String.valueOf(ty.getValue()));
            doc=new Document();
            doc.add(new Field("id",key, Field.Store.YES, Field.Index.NOT_ANALYZED));
            doc.add(new Field("category",category, Field.Store.YES, Field.Index.NOT_ANALYZED));
            for(Map.Entry<String, Object> ob:obj.entrySet()){
                //Field field = new TextField(ob.getKey(), String.valueOf(ob.getValue()), Field.Store.YES);
                Field field =LuceneFormatter.initialFormatter(ob.getKey(),String.valueOf(ob.getValue()),category);
                doc.add(field);
              /*  doc.add(LuceneFormatter.initialFormatter(ob.getKey(),String.valueOf(ob.getValue())));*/
            }
            try {
                IKAnalyzer  ikAnalyzer=new IKAnalyzer(true);
                Term  term=new Term("id",key);
                indexWriter.updateDocument(term,doc,ikAnalyzer);
                indexWriter.commit();
                //indexWriter.addDocument(doc);
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

    @Override
    public void convertHandlerDocument(ResourceAttribute attribute) {
        IndexWriter indexWriter = attribute.getWriter();
        RedisSource  rs= (RedisSource) attribute;
        String keys=rs.getCategory();
        if(StringUtils.isNotEmpty(keys)){
            Map<Object, Object>  map=cacheUtils.hgetAll(keys);
            createIndex(indexWriter,map,attribute.getCategory());
        }
    }
}
