package cn.begonia.lucene.jaslucene.service.search.impl;

import cn.begonia.lucene.jaslucene.common.QueryCondition;
import cn.begonia.lucene.jaslucene.common.Result;
import cn.begonia.lucene.jaslucene.common.SearchType;
import cn.begonia.lucene.jaslucene.famatter.LuceneFormatter;
import cn.begonia.lucene.jaslucene.service.handler.LuceneReaderService;
import cn.begonia.lucene.jaslucene.service.search.ISearchService;
import cn.begonia.lucene.jaslucene.util.DateUtils;
import com.alibaba.fastjson.JSONObject;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.*;
import org.apache.lucene.util.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
    public Result defaultAllCategorySearch(QueryCondition queryParser) {
        List<JSONObject> result=new ArrayList<>();
        /**打开所有的索引目录*/
        try {
            return luceneReaderService.multiIndexQuery(queryParser);
            /* luceneReaderService.openResource(index);*/
           /* luceneReaderService.changeResource(category);
            String [] str=LuceneFormatter.listFields();
            // Result  res=luceneReaderService.multiFieldQueryParser(str,keyword);
            Result  res =luceneReaderService.numericQuery("date",DateUtils.getDefaultDate());
            result.addAll((Collection<? extends JSONObject>) res.getObj());*/
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //luceneReaderService.closeReader();
        }
        return Result.isOk(result);
    }

    /** 在分类中推荐24小时内容 **/
    @Override
    public Result defaultCategorySearch(QueryCondition queryParser) {
        Result  result=null;
        try {
            SortField  date=new SortField("date",SortField.Type.LONG,true);
            Sort  sort=new Sort(date);
            queryParser.setSort(sort);
            result =luceneReaderService.queryNumericResult("date",DateUtils.getDefaultDate(),queryParser);
            //Result rest=luceneReaderService.openResourceByDay(queryParser.getCategory());
            //if(rest.getCode()==200){
            //}else {
                //return  rest;
            //}
           // luceneReaderService.closeReader();
           return  result;
        } catch (ParseException e) {
            e.printStackTrace();
            return  Result.isFail();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    /** 在所有的索引中查询**/
    @Override
    public Result defaultKeywordSearch(QueryCondition queryParser) {
        List<JSONObject> result=new ArrayList<>();
        /**打开所有的索引目录  废弃这种切换方式begonia 20201103 */
      /*  for(String category:SearchType.list()){
            luceneReaderService.changeResource(category);
            String [] str=LuceneFormatter.listFields();
            Result  res=luceneReaderService.multiFieldQueryParser(str,keyword);
            result.addAll((Collection<? extends JSONObject>) res.getObj());
        }*/
        try {
           return luceneReaderService.multiKeywordQuery(new QueryCondition(queryParser.getKeyword(),""));
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //luceneReaderService.closeReader();
        return Result.isOk(result);
    }

    @Override
    public Result search(QueryCondition queryParser) {
        if(!SearchType.validationCategory(queryParser.getCategory())){
            return defaultKeywordSearch(queryParser);
        }
        luceneReaderService.openResource(queryParser.getCategory());
        Result result=luceneReaderService.multiFieldQueryParser(LuceneFormatter.listFields(),queryParser.getKeyword(),queryParser);
        return  result;
    }

}
