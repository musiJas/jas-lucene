package cn.begonia.lucene.jaslucene.common;

import com.alibaba.fastjson.JSONObject;

import java.util.*;

/**
 * @author begonia_chen
 * @data 2020/8/19 10:55
 * @description  查找的分类
 **/
public enum SearchType {
    hotspot("hotspot","热点数据"),
    blog("cnblogs","博客类型"),
    life("life","生活类-包含常识等"),
    reading("reading","阅读指导"),
    movie("movie","高分movie指导"),
    journey("journey","旅行指导");

    private  String  category;
    private  String  description;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    SearchType(String category, String   description){
        this.category=category;
        this.description=description;
    }

    public static boolean  validationCategory(String  category){
        List<String> list=new ArrayList<>();
        for(SearchType type:SearchType.values()){
            list.add(type.getCategory());
        }
        return list.contains(category);
    }

    public static List<String>  list(){
        List<String> list=new ArrayList<>();
        for(SearchType type:SearchType.values()){
            list.add(type.getCategory());
        }
        return list;
    }


    public static JSONObject listObject(){
         JSONObject  map=new JSONObject(true);
        for(SearchType type:SearchType.values()){
            System.out.println(type.getCategory());
            map.put(type.getCategory(),type.getDescription());
        }
        return map;
    }
}
