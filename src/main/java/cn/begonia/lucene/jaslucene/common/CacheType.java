package cn.begonia.lucene.jaslucene.common;

/**
 * @author begonia_chen
 * @data 2020/8/19 9:13
 * @description 缓存等主要关键词
 **/
public enum   CacheType {

     cnblogs("cnblogs","博客类型"),
     hotspot("hotspot","热点信息");

    private  String  key;
    private  String  description;


    CacheType(String key,String description) {
        this.key=key;
        this.description=description;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }



}
