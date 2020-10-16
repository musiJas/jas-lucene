package cn.begonia.lucene.jaslucene.web;

import cn.begonia.lucene.jaslucene.common.QueryCondition;
import cn.begonia.lucene.jaslucene.common.Result;
import cn.begonia.lucene.jaslucene.service.search.ISearchService;
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
            @RequestParam(value = "page",required = false, defaultValue = "0") int  page,
            @RequestParam(value = "pageSize",required = false,defaultValue = "0") int pageSize
    ){
        Result  res=null;
        ModelAndView  modelAndView=new ModelAndView();
        /** 先判断关键词是否为空 如果为空则以默认条件进行检索即时间检索**/
        if(StringUtils.isEmpty(keyword)){
            if(StringUtils.isEmpty(category)){
                res =searchService.defaultAllCategorySearch(new QueryCondition(page,pageSize));
            }
            res =searchService.defaultCategorySearch(category);
        }else {
            if(StringUtils.isEmpty(category)){
                 res=  searchService.defaultKeywordSearch(keyword);
            }else{
                res= searchService.search(keyword,category);
            }
        }
        modelAndView.setViewName("result");
        if(StringUtils.isNotEmpty(keyword)){
            modelAndView.addObject("keyword",keyword);
        }

        modelAndView.addObject("data",res.getObj());
        return modelAndView;
    }


    @RequestMapping("/api/search")
    @ResponseBody
    public Result searchApi(
            @RequestParam(value = "keyword",required = false) String keyword,
            @RequestParam(value = "category",required = false) String category,
            @RequestParam(value = "page",required = false, defaultValue = "0") int  page,
            @RequestParam(value = "pageSize",required = false,defaultValue = "0") int pageSize
    ){
        Result  res=null;
        ModelAndView  modelAndView=new ModelAndView();
        /** 先判断关键词是否为空 如果为空则以默认条件进行检索即时间检索**/
        if(StringUtils.isEmpty(keyword)){
            if(StringUtils.isEmpty(category)){
                res =searchService.defaultAllCategorySearch(new QueryCondition(page,pageSize));
            }
            res =searchService.defaultCategorySearch(category);
        }else {
            if(StringUtils.isEmpty(category)){
                res=  searchService.defaultKeywordSearch(keyword);
            }else{
                res= searchService.search(keyword,category);
            }
        }
        return res;
    }

}
