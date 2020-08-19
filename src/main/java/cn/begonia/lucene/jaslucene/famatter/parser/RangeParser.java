package cn.begonia.lucene.jaslucene.famatter.parser;


import cn.begonia.lucene.jaslucene.util.DateUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;

import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

/**
 * @author begonia_chen
 * @data 2020/8/18 16:15
 * @description 范围查找
 **/
public class RangeParser  extends QueryParser {


    public RangeParser(Version matchVersion, String f, Analyzer a) {
        super(matchVersion, f, a);
    }



    @Override
    protected Query getRangeQuery(String field, String part1, String part2, boolean startInclusive, boolean endInclusive) throws ParseException {
        if("like".equals(field)||"browse".equals(field)||"comment".equals(field)){
            return NumericRangeQuery.newIntRange(field,Integer.parseInt(part1),Integer.parseInt(part2),startInclusive,endInclusive);
        }else if("date".equals(field)) {
            Pattern pattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
            if (pattern.matcher(part1).matches() && pattern.matcher(part2).matches()) {
                try {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    System.out.println(part1);
                    long start = DateUtils.parse(part1,"yyyy-MM-dd").getTime();
                    long end =DateUtils.parse(part2,"yyyy-MM-dd").getTime();
                    System.out.println(start+":"+end);
                    return  NumericRangeQuery.newLongRange(field,start,end,startInclusive,endInclusive);
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }

            }else{
                throw   new  ParseException("日期格式不对");
            }
        }else{
            super.getRangeQuery(field,part1,part2,startInclusive,endInclusive);
        }
        return super.getRangeQuery(field, part1, part2, startInclusive, endInclusive);
    }
}
