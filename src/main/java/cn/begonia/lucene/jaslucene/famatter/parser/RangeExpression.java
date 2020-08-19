package cn.begonia.lucene.jaslucene.famatter.parser;

import cn.begonia.lucene.jaslucene.famatter.exception.ArgumentIsNullException;
import org.apache.commons.lang.StringUtils;

import java.text.ParseException;

/**
 * @author begonia_chen
 * @data 2020/8/18 17:32
 * @description   生成range查询表达式
 *
 **/
public class RangeExpression {

    public static String  composeExpression(String field,String start,String end){
        StringBuffer sb=new StringBuffer();
        if(StringUtils.isEmpty(field)||StringUtils.isEmpty(start)||StringUtils.isEmpty(end)){
            throw  new ArgumentIsNullException("主要参数不允许为空,请检查....");
        }
        sb.append(field).append(":").append("[").append(start).append(" TO ").append(end).append("]");
        return sb.toString();
    }

    public  static  String  composeExpression(String field,String value){
        StringBuffer sb=new StringBuffer();
        if(StringUtils.isEmpty(field)||StringUtils.isEmpty(field)||StringUtils.isEmpty(value)){
            throw  new ArgumentIsNullException("主要参数不允许为空,请检查....");
        }
        sb.append(field).append(":").append(value);
        return sb.toString();
    }

}
