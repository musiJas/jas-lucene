package cn.begonia.lucene.jaslucene.famatter;

import cn.begonia.lucene.jaslucene.util.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.document.*;
import org.apache.lucene.document.Field.Store;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author begonia_chen
 * @data 2020/8/18 13:03
 * @description
 **/
public enum LuceneFormatter {
    title("title", TextField.class, Field.Store.YES,Field.Index.ANALYZED,5.0f),
    url("url", StringField.class,Field.Store.YES,Field.Index.NOT_ANALYZED,1.0f),
    auth("auth",TextField.class,Field.Store.YES, Field.Index.ANALYZED_NO_NORMS,2.0f),
    date("date", LongField.class,Field.Store.YES,Field.Index.ANALYZED,1.0f),
    like("like", IntField.class,Field.Store.YES,Field.Index.NOT_ANALYZED,3.0f),
    comment("comment", IntField.class,Field.Store.YES,Field.Index.NOT_ANALYZED,1.0f),
    browse("browse",IntField.class,Field.Store.YES,Field.Index.NOT_ANALYZED,1.0f),
    content("content",TextField.class, Field.Store.YES,Field.Index.ANALYZED,3.0f),
    img("img",TextField.class,Field.Store.YES, Field.Index.NOT_ANALYZED,1.0f),
    focus("focus",IntField.class,Field.Store.YES, Field.Index.NOT_ANALYZED,1.0f),
    rank("rank",IntField.class,Field.Store.YES, Field.Index.NOT_ANALYZED,1.0f);

    private  String  field;
    private  Class  fieldType;
    private  Field.Store  storeValue;
    private  Field.Index  indexValue;
    private  float    boost;
    LuceneFormatter(){

    }


    LuceneFormatter(String field, Class fieldType, Field.Store  storeValue, Field.Index indexValue, Float  boost){
        this.field=field;
        this.fieldType=fieldType;
        this.storeValue=storeValue;
        this.indexValue=indexValue;
        this.boost=boost;
    }


    public static  String[]  listFields(){
        List<String> list=new ArrayList<>();
        for(LuceneFormatter matter:LuceneFormatter.values()){
            list.add(matter.field);
        }
        return  list.toArray(new String[list.size()]);
    }


    public static  Field   initialFormatter(String field,String value){
        /*Field  field1=new TextField("","",null);
        field1.setBoost(5.0f);*/
        for(LuceneFormatter bf: LuceneFormatter.values()){
            if(StringUtils.equals(field,bf.field)){
                    try {
                        Constructor<Field> constructor=null;
                        Field fs=null;
                        if(StringUtils.equals(bf.fieldType.getName(),IntField.class.getName())){
                            constructor=bf.fieldType.getConstructor(String.class,int.class, Store.class);
                            fs= constructor.newInstance(field,Integer.parseInt(value),bf.storeValue);
                        }else if(StringUtils.equals(bf.fieldType.getName(),LongField.class.getName())){
                            constructor=bf.fieldType.getConstructor(String.class,long.class, Store.class);
                            fs= constructor.newInstance(field, DateUtils.parse(value,"yyyy-MM-dd HH:mm").getTime(),bf.storeValue);
                        }else {
                            constructor=bf.fieldType.getConstructor(String.class,String.class, Store.class);
                            fs= constructor.newInstance(field,value,bf.storeValue);
                        }
                        fs.setBoost(bf.boost);// 更新权重,默认是1.0f
                        return  fs;

                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }catch (ParseException e) {
                        e.printStackTrace();
                    }

            }
        }
        return null;
    }


}
