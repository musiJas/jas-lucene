package cn.begonia.lucene.jaslucene.util;

import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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


    /** 获取当前给定时间的往前24小时的日期区间 **/
    public static  String   getDurationTime(Date date){
        if(date == null){
            return "";
        }
        Calendar  cal=Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH,-30);
        Date  yestoday=cal.getTime();

        StringBuffer  sb=new StringBuffer();
        sb.append("date:[");
        sb.append(format(yestoday,"yyyy-MM-dd "));
        sb.append(" TO ").append(format(date,"yyyy-MM-dd"));
        sb.append("]");
        return sb.toString();
    }

    public  static String   getDefaultDate(){
        return  getDurationTime(new Date());
    }


    public static void main(String[] args) throws ParseException {

        System.out.println(getDefaultDate());

       // System.out.println(new DateUtils().format(new Date()));
        //System.out.println(DateUtils.parse("2020-08-18"));


    }
}
