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
        return  luceneReaderService.numericQuery("","");
    }

    @Override
    public Result defaultKeywordSearch(String keyword) {
        return null;
    }

    @Override
    public Result search(String category, String keyword) {
        if(!SearchType.validationCategory(category)){
            return defaultKeywordSearch(keyword);
        }
        return  luceneReaderService.multiFieldQueryParser(LuceneFormatter.listFields(),keyword);
    }
}
