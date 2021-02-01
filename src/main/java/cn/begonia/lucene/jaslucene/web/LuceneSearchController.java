package cn.begonia.lucene.jaslucene.web;

import cn.begonia.lucene.jaslucene.common.QueryCondition;
import cn.begonia.lucene.jaslucene.common.Result;
import cn.begonia.lucene.jaslucene.service.search.ISearchService;
import cn.begonia.lucene.jaslucene.util.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author begonia_chen
 * @data 2020/8/17 18:01
 * @description
 **/
@SuppressWarnings("all")
@Controller
public class LuceneSearchController {
    private ISearchService  searchService;
    @Autowired
    public  LuceneSearchController(ISearchService  searchService){
        this.searchService=searchService;
    }

    @RequestMapping("/search")
    public ModelAndView search(
            @RequestParam(value = "keyword",required = false) String keyword,
            @RequestParam(value = "category",required = false) String category,
            @RequestParam(value = "page",required = false, defaultValue = "1") int  page,
            @RequestParam(value = "pageSize",required = false,defaultValue = "30") int pageSize
    ){
        long startTime=System.currentTimeMillis();
        Result  res=null;
        ModelAndView  modelAndView=new ModelAndView();
        /** 先判断关键词是否为空 如果为空则以默认条件进行检索即时间检索**/
        if(StringUtils.isEmpty(keyword)){
            if(StringUtils.isEmpty(category)){
                res =searchService.defaultAllCategorySearch(new QueryCondition(page,pageSize));
            }else {
                QueryCondition   queryCondition=new QueryCondition(page,pageSize);
                queryCondition.setCategory(category);
                res =searchService.defaultCategorySearch(queryCondition);
            }
        }else {
            if(StringUtils.isEmpty(category)){
                 QueryCondition   queryCondition=new QueryCondition(page,pageSize);
                 queryCondition.setKeyword(keyword);
                 res=  searchService.defaultKeywordSearch(queryCondition);
            }else{
                QueryCondition   queryCondition=new QueryCondition(page,pageSize);
                queryCondition.setKeyword(keyword);
                queryCondition.setCategory(category);
                res= searchService.search(queryCondition);
            }
        }
        modelAndView.setViewName("result");
        if(StringUtils.isNotEmpty(keyword)){
            modelAndView.addObject("keyword",keyword);
        }
        if(category!=null && StringUtils.isNotBlank(category)){
            modelAndView.addObject("category",category);
        }
        modelAndView.addObject("data",res.getObj());
        long endTime=System.currentTimeMillis();
        modelAndView.addObject("time", DateUtils.getTime(startTime,endTime));
        modelAndView.addObject("total",res.getTotal());
        /**组装nextPageKey*/
        StringBuffer sb=new StringBuffer("?");
        sb.append("keyword=").append(keyword).append("&");
        sb.append("category=").append(category).append("&");
        sb.append("page=").append(page+1).append("&");
        sb.append("pageSize=").append(pageSize);
        modelAndView.addObject("href",sb.toString());
        return modelAndView;
    }


    @RequestMapping("/api/search")
    @ResponseBody
    public Result searchApi(
            @RequestParam(value = "keyword",required = false) String keyword,
            @RequestParam(value = "category",required = false) String category,
            @RequestParam(value = "page",required = false, defaultValue = "0") int  page,
            @RequestParam(value = "pageSize",required = false,defaultValue = "20") int pageSize
    ){
        Result  res=null;
        ModelAndView  modelAndView=new ModelAndView();
        /** 先判断关键词是否为空 如果为空则以默认条件进行检索即时间检索**/
        if(StringUtils.isEmpty(keyword)){
            if(StringUtils.isEmpty(category)){
                res =searchService.defaultAllCategorySearch(new QueryCondition(page,pageSize));
            }else {
                QueryCondition   queryCondition=new QueryCondition(page,pageSize);
                queryCondition.setCategory(category);
                res =searchService.defaultCategorySearch(queryCondition);
            }
        }else {
            if(StringUtils.isEmpty(category)){
                QueryCondition   queryCondition=new QueryCondition(page,pageSize);
                queryCondition.setKeyword(keyword);
                res=  searchService.defaultKeywordSearch(queryCondition);
            }else{
                QueryCondition   queryCondition=new QueryCondition(page,pageSize);
                queryCondition.setKeyword(keyword);
                queryCondition.setCategory(category);
                res= searchService.search(queryCondition);
            }
        }
        return res;
    }


    @RequestMapping("/api/searchByDay")
    @ResponseBody
    public Result searchByToDay(
            @RequestParam(value = "category",required = true) String category,
            @RequestParam(value = "page",required = false, defaultValue = "0") int  page,
            @RequestParam(value = "pageSize",required = false,defaultValue = "20") int pageSize
    ) {
        QueryCondition   queryCondition=new QueryCondition(page,pageSize);
        queryCondition.setCategory(category);
        queryCondition.setPage(page);
        queryCondition.setPageSize(pageSize);
        Result res =searchService.defaultCategorySearch(queryCondition);
        return res;
    }
}
