package cn.begonia.lucene.jaslucene.common;

import lombok.Data;
import org.apache.lucene.search.Sort;
import java.io.Serializable;
import java.util.Map;

/**
 * @author begonia_chen
 * @data 2020/9/7 17:40
 * @description  查询器 含返回值
 **/
@Data
public class QueryCondition implements Serializable {
    public  static final  Integer PAGESIZE=30;
    private  Integer  page;
    private  Integer  pageSize;
    private  Map<String,Object> map;
    private  Sort  sort;
    private  String keyword;
    private  String category;

    public  QueryCondition(){

    }

    public  QueryCondition(int page,int pageSize){
        this.page=page;
        this.pageSize=pageSize;
    }

    public  QueryCondition(String keyword,String category){
        this.keyword=keyword;
        this.category=category;
    }
    public  QueryCondition(int page,String keyword,String  category){
        this.page=page;
        this.pageSize=PAGESIZE;
        this.keyword=keyword;
        this.category=category;
    }
    public  QueryCondition(int page,int pageSize,String keyword,String  category){
        this.page=page;
        this.pageSize=pageSize;
        this.keyword=keyword;
        this.category=category;
    }

     public static QueryCondition  initKeyWordParser(String  keyword){
         QueryCondition  parser=new QueryCondition();
         parser.setKeyword(keyword);
        return  parser;
    }
    public static QueryCondition  initCategoryParser(String  category){
        QueryCondition  parser=new QueryCondition();
        parser.setCategory(category);
        return  parser;
    }

    public static QueryCondition  newInstance(int page, String keyword,String category){
        return  new QueryCondition(page,keyword,category);
    }

    public static QueryCondition  newInstance(int page,int pageSize){
        return  new QueryCondition(page,pageSize);
    }


    public static void main(String[] args) {

    }

}
