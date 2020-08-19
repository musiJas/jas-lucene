package cn.begonia.lucene.jaslucene.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author begonia_chen
 * @data 2020/8/18 14:55
 * @description
 **/
public class DateUtils {
    private static SimpleDateFormat  simpleDateFormat=new SimpleDateFormat();
    private static  ThreadLocal<SimpleDateFormat>  local=new ThreadLocal<>();

    public static String  format(Date  date){
        local.set(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        //String format="yyyy-MM-dd HH:mm:ss";
        return  local.get().format(date);
    }
    public static String  format(Date  date,String  formatter){
        local.set(new SimpleDateFormat(formatter));
        //String format="yyyy-MM-dd HH:mm:ss";
        return  local.get().format(date);
    }


    public static  Date  parse(String date) throws ParseException {
        local.set(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        return local.get().parse(date);
    }

    public static  String  parseTimeMillis(String  timeMillis){
       long time= Long.parseLong(timeMillis);
       return  format(new Date(time),"yyyy-MM-dd HH:mm");
    }

    public static  Date  parse(String date,String formatter) throws ParseException {
        local.set(new SimpleDateFormat(formatter));
        return local.get().parse(date);
    }


    public static  String  getDefaultNowTime(){
        return  format(new Date());
    }


    public static void main(String[] args) throws ParseException {



       // System.out.println(new DateUtils().format(new Date()));
        System.out.println(DateUtils.parse("2020-08-18"));


    }
}
