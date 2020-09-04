package cn.begonia.lucene.jaslucene.web;

import cn.begonia.lucene.jaslucene.common.Result;
import cn.begonia.lucene.jaslucene.service.handler.LuceneReaderService;
import cn.begonia.lucene.jaslucene.service.search.ISearchService;
import cn.begonia.lucene.jaslucene.util.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author begonia_chen
 * @data 2020/8/17 18:01
 * @description
 **/

@RestController
public class LuceneSearchController {
    private ISearchService  searchService;
    @Autowired
    public  LuceneSearchController(ISearchService  searchService){
        this.searchService=searchService;
    }

    @RequestMapping("/search")
    public Result  search(@RequestParam(value = "keyword",required = false) String keyword,@RequestParam(value = "category",required = false) String category){
        //luceneReaderService.termQuery("titles",key);
        /** 先判断关键词是否为空 如果为空则以默认条件进行检索即时间检索**/
        if(StringUtils.isEmpty(keyword)){
            if(StringUtils.isEmpty(category)){
                return  searchService.defaultAllCategorySearch();
            }
            return  searchService.defaultCategorySearch(category);
        }else {
            if(StringUtils.isEmpty(category)){
                return  searchService.defaultKeywordSearch(keyword);
            }
            return searchService.search(keyword,category);
        }
    }




}
