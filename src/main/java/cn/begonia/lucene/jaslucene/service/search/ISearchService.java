package cn.begonia.lucene.jaslucene.service.search;

import cn.begonia.lucene.jaslucene.common.Result;

/**
 * @author begonia_chen
 * @data 2020/8/19 11:09
 * @description   fullText search  service
 *
 **/
public interface ISearchService {

    /**
     * 默认的全目录查询 意味着关键词和分类为空
     * **/
    Result  defaultAllCategorySearch();
    /**
     * 默认的目录查询 意味着关键词为空
     * 在当前的分类中按照时间的前后推荐24小时内容
     * **/
    Result  defaultCategorySearch(String  category);
    /**
     * 默认的关键词查询 意味着分类为空
     * 则在所有的索引目录下进行关键词查询,精确查询
     * **/
    Result  defaultKeywordSearch(String keyword);
    /**
     * 默认的全文查找查询 意味着关键词和分类都有
     * 则在该分类下进行关键词检索
     * **/
    Result  search(String keyword,String category);
}
