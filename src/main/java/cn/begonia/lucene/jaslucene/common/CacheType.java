package cn.begonia.lucene.jaslucene.common;

import cn.begonia.lucene.jaslucene.famatter.formatter.*;

/**
 * @author begonia_chen
 * @data 2020/8/19 9:13
 * @description 缓存等主要关键词
 **/
public enum   CacheType {

    hotspot("hotspot","热点信息", HotspotFormatter.class),
    cnblogs("cnblogs","博客类型", CnblogsFormatter.class),
     life("life","生活类-包含常识等", CacheType.class),
     reading("reading","阅读指导", ReadingFormatter.class),
     movie("movie","高分movie指导", MovieFormatter.class),
     weibo("weibo","微博", WeiboFormatter.class),
     journey("journey","旅行指导",JourneyFormatter.class);
    private  String  key;
    private  String  description;
    private  Class  cls;


    CacheType(String key,String description,Class cls) {
        this.key=key;
        this.description=description;
        this.cls=cls;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Class getCls() {
        return cls;
    }

    public void setCls(Class cls) {
        this.cls = cls;
    }



}
