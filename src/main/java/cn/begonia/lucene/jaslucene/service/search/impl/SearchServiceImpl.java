package cn.begonia.lucene.jaslucene.service.search.impl;

import cn.begonia.lucene.jaslucene.common.Result;
import cn.begonia.lucene.jaslucene.common.SearchType;
import cn.begonia.lucene.jaslucene.famatter.LuceneFormatter;
import cn.begonia.lucene.jaslucene.service.handler.LuceneReaderService;
import cn.begonia.lucene.jaslucene.service.search.ISearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author begonia_chen
 * @data 2020/8/19 11:09
 * @description
 **/
@Service
public class SearchServiceImpl  implements ISearchService {
    private LuceneReaderService  luceneReaderService;
    @Autowired
    public  SearchServiceImpl(LuceneReaderService luceneReaderService){
        this.luceneReaderService=luceneReaderService;
    }

    @Override
    public Result defaultAllCategorySearch() {

        return null;
    }

    @Override
    public Result defaultCategorySearch(String category) {
        //return  luceneReaderService.numericQuery("","");
        return  null;
    }

    /** 在所有的索引中查询**/
    @Override
    public Result defaultKeywordSearch(String keyword) {
        /**打开所有的索引目录*/
        for(String index:SearchType.list()){
            luceneReaderService.openResource(index);



        }

        return null;
    }

    @Override
    public Result search(String category, String keyword) {
        if(!SearchType.validationCategory(category)){
            return defaultKeywordSearch(keyword);
        }
        luceneReaderService.openResource(category);
        Result result=luceneReaderService.multiFieldQueryParser(LuceneFormatter.listFields(),keyword);
        return  result;
    }
}
