package cn.begonia.lucene.jaslucene.famatter;

import cn.begonia.lucene.jaslucene.common.CacheType;
import cn.begonia.lucene.jaslucene.famatter.formatter.*;
import cn.begonia.lucene.jaslucene.util.DateUtils;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.document.*;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.*;

/**
 * @author begonia_chen
 * @data 2020/8/18 13:03
 * @description
 **/
@SuppressWarnings("all")
public enum LuceneFormatter  {
 /*   Field.Index.ANALYZED:分词建索引
    Field.Index.ANALYZED_NO_NORMS:分词建索引，但是Field的值不像通常那样被保存，而是只取一个byte，这样节约存储空间(ANALYZED存储了index time,boost information等norms，而ANALYZED_NO_NORMS不存储)
    Field.Index.NOT_ANALYZED:不分词且索引,即不使用 analyzer分析，整体作为一个token，常用语精确匹配，例如文件名，ID号等就用这个
    Field.Index.NOT_ANALYZED_NO_NORMS:不分词建索引，Field的值去一个byte保存*/

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
    rank("rank",IntField.class,Field.Store.YES, Field.Index.NOT_ANALYZED,1.0f),

    score("score",StringField.class,Field.Store.YES, Field.Index.NOT_ANALYZED,1.0f),
    star("star",StringField.class,Field.Store.YES, Field.Index.NOT_ANALYZED,1.0f),
    release("release",StringField.class,Field.Store.YES, Field.Index.NOT_ANALYZED,1.0f),
    duration("duration",TextField.class,Field.Store.YES, Field.Index.NOT_ANALYZED,3.0f),
    region("region",TextField.class,Field.Store.YES, Field.Index.ANALYZED,1.0f),
    director("director",TextField.class,Field.Store.YES, Field.Index.NOT_ANALYZED,1.0f),
    actor("actor",TextField.class,Field.Store.YES, Field.Index.NOT_ANALYZED,5.0f),
    info("info",TextField.class,Field.Store.YES, Field.Index.ANALYZED_NO_NORMS,1.0f),
    id("id",IntField.class,Field.Store.YES, Field.Index.NOT_ANALYZED,1.0f),
    category("category",TextField.class,Field.Store.YES, Field.Index.NOT_ANALYZED,1.0f),
    releaseDate("releaseDate",TextField.class,Field.Store.YES, Field.Index.NOT_ANALYZED,1.0f),
    author("author",TextField.class,Field.Store.YES, Field.Index.NOT_ANALYZED,1.0f),
    detail("detail",TextField.class,Field.Store.YES, Field.Index.NOT_ANALYZED,1.0f),
    type("type",TextField.class,Field.Store.YES, Field.Index.NOT_ANALYZED,1.0f);


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
      /*  for(LuceneFormatter matter:LuceneFormatter.values()){
            list.add(matter.field);
        }*/
        for(CacheType type:CacheType.values()){
            list.addAll(getCategoryAllField(type.getKey()));
        }
        list = new ArrayList<>(new HashSet<>(list));
        return  list.toArray(new String[list.size()]);
    }

    public static JSONObject  getReturnJson(String category,Document  document){
        if(StringUtils.equals(CacheType.cnblogs.getKey(),category)){
             return    CnblogsFormatter.resolveDocument(document);
        }else if(StringUtils.equals(CacheType.hotspot.getKey(),category)) {
            return    HotspotFormatter.resolveDocument(document);
        }else if(StringUtils.equals(CacheType.movie.getKey(),category)){
            return    MovieFormatter.resolveDocument(document);
        }else if(StringUtils.equals(CacheType.reading.getKey(),category)){
            return    ReadingFormatter.resolveDocument(document);
        }else if(StringUtils.equals(CacheType.life.getKey(),category)){
            return    LifeFormatter.resolveDocument(document);
        }else if(StringUtils.equals(CacheType.journey.getKey(),category)){
            return    JourneyFormatter.resolveDocument(document);
        }else if(StringUtils.equals(CacheType.weibo.getKey(),category)){
            return    WeiboFormatter.resolveDocument(document);
        }
        return null;
    }

    public  static  List<String>  getCategoryAllField(String  category){
        List<String> list=new ArrayList<>();
        if(StringUtils.equals(CacheType.cnblogs.getKey(),category)){
            String[] arr=CnblogsFormatter.listFields();
            list= Arrays.asList(arr);
        }else if(StringUtils.equals(CacheType.hotspot.getKey(),category)) {
            String[] arr=HotspotFormatter.listFields();
            list=Arrays.asList(arr);
        }else if(StringUtils.equals(CacheType.movie.getKey(),category)){
            String[] arr=MovieFormatter.listFields();
            list=Arrays.asList(arr);
        }else if(StringUtils.equals(CacheType.reading.getKey(),category)){
            String[] arr=ReadingFormatter.listFields();
            list=Arrays.asList(arr);
        }else if(StringUtils.equals(CacheType.life.getKey(),category)){
            String[] arr=LifeFormatter.listFields();
            list=Arrays.asList(arr);
        }else if(StringUtils.equals(CacheType.journey.getKey(),category)){
            String[] arr=JourneyFormatter.listFields();
            list=Arrays.asList(arr);
        }else if(StringUtils.equals(CacheType.weibo.getKey(),category)){
            String[] arr=WeiboFormatter.listFields();
            list=Arrays.asList(arr);
        }
        return list;
    }


    public static Sort   getDefaultSort(String  category){
        if(StringUtils.equals(CacheType.cnblogs.getKey(),category)){
            return CnblogsFormatter.getDefaultSort();
        }else if(StringUtils.equals(CacheType.hotspot.getKey(),category)) {
            return    HotspotFormatter.getDefaultSort();
        }else if(StringUtils.equals(CacheType.movie.getKey(),category)){
            return    MovieFormatter.getDefaultSort();
        }else if(StringUtils.equals(CacheType.reading.getKey(),category)){
            return    ReadingFormatter.getDefaultSort();
        }else if(StringUtils.equals(CacheType.life.getKey(),category)){
            return    LifeFormatter.getDefaultSort();
        }else if(StringUtils.equals(CacheType.journey.getKey(),category)){
            return    JourneyFormatter.getDefaultSort();
        }else if(StringUtils.equals(CacheType.weibo.getKey(),category)){
            return    WeiboFormatter.getDefaultSort();
        }else {
            /**默认的排序规则**/
            SortField  date =new SortField("date",SortField.Type.LONG,true);
            Sort  sort=new Sort(date);
            return  sort;
        }
    }



    public static  Field   initialFormatter(String field,String value,String category){
        /*Field  field1=new TextField("","",null);
        field1.setBoost(5.0f);*/
        if(StringUtils.equals(CacheType.cnblogs.getKey(),category)){
            return    CnblogsFormatter.initialFormatter(field,value);
        }else if(StringUtils.equals(CacheType.hotspot.getKey(),category)) {
            return    HotspotFormatter.initialFormatter(field,value);
        }else if(StringUtils.equals(CacheType.movie.getKey(),category)){
            return    MovieFormatter.initialFormatter(field,value);
        }else if(StringUtils.equals(CacheType.reading.getKey(),category)){
            return    ReadingFormatter.initialFormatter(field,value);
        }else if(StringUtils.equals(CacheType.life.getKey(),category)){
            return    LifeFormatter.initialFormatter(field,value);
        }else if(StringUtils.equals(CacheType.journey.getKey(),category)){
            return    JourneyFormatter.initialFormatter(field,value);
        }else if(StringUtils.equals(CacheType.weibo.getKey(),category)){
            return    WeiboFormatter.initialFormatter(field,value);
        }
        return null;
    }

    public  static   Field   getField(String field,String value, Map<String,Object> map,Class cls){
        String bfField=String.valueOf(map.get("field"));
        Field.Store  storeValue= (Field.Store) map.get("storeValue");
        Field.Index  indexValue= (Field.Index) map.get("indexValue");
        Float  boost= (Float) map.get("boost");
        try {
            Constructor<Field> constructor=null;
            Field fs=null;
            if(StringUtils.equals(cls.getName(),IntField.class.getName())){
                constructor=cls.getConstructor(String.class,int.class, Field.Store.class);
                fs= constructor.newInstance(field,Integer.parseInt(value),storeValue);
            }else if(StringUtils.equals(cls.getName(),LongField.class.getName())){
                constructor=cls.getConstructor(String.class,long.class, Field.Store.class);
                fs= constructor.newInstance(field, DateUtils.parse(value,"yyyy-MM-dd HH:mm").getTime(),storeValue);
            }else if(StringUtils.equals(cls.getName(),StringField.class.getName())){
                constructor=cls.getConstructor(String.class,String.class, Field.Store.class);
                fs= constructor.newInstance(field,value,storeValue);
                fs.setBoost(boost);// 更新权重,默认是1.0f
            }else if(StringUtils.equals(cls.getName(),TextField.class.getName())){
                constructor=cls.getConstructor(String.class,String.class, Field.Store.class);
                fs= constructor.newInstance(field,value,storeValue);
                fs.setBoost(boost);// 更新权重,默认是1.0f
            }
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
        return null;
    }


     public static  JSONObject  convertObject(String key, Document document){
        JSONObject obj = new JSONObject();
         /**对非正常格式做一下数据处理**/
         if(StringUtils.equals(key,CnblogsFormatter.content.name())){
             String  content=document.get(key);
             if(StringUtils.isEmpty(content)){
                 return null;
             }
             if(content.startsWith(">")||content.startsWith("<")||content.endsWith("</p>")){
                 content=content.substring(1,content.length()).replaceAll("</p>","");
                 // content=content.replace(">","").replace("<","").replaceAll("</p>","");
             }
             if(content.indexOf("avatar")!=-1){
                 //content= content.replace("\\n","");
                 String avatar=content.substring(0,content.indexOf("/>")-1);
                 avatar=avatar.substring(avatar.indexOf("src")+5);
                 content=content.substring(content.indexOf("/>")+2);
                 content=content.replaceAll("\\n","").trim();
                 obj.put("avatar",avatar);
                 obj.put("content",content);
             }else {
                 content=content.replaceAll("\\n","").trim();
                 obj.put("content",content);
             }
         }else if(StringUtils.equals(key,CnblogsFormatter.date.name())){
             long   longDate=Long.parseLong(document.get(key));
             obj.put(key,DateUtils.format(new Date(longDate)));
         }else {
             obj.put(key,document.get(key));
         }
         return  obj;
     }

    public  static  JSONObject  judgeShowDescription(JSONObject  obj){
        String avatar=obj.getString("avatar");
        String img=obj.getString("img");
        String content=obj.getString("content");
        String detail=obj.getString("detail");
        if(StringUtils.isEmpty(avatar)||avatar.length()==0){
             if(StringUtils.isEmpty(img)){
                 if(StringUtils.isNotBlank(detail)||StringUtils.isNotBlank(content)){
                     obj.put("show",true);
                     obj.put("display",false);
                 }else {
                     obj.put("show",false);
                 }
                 return obj;
             }
        }
        obj.put("show",true);
        obj.put("display",true);
        return  obj;
    }


    public  static  JSONObject   convert(String field,Document  document){
        JSONObject json=new JSONObject();
        if(StringUtils.equals(field,"date")){
            Long  times=Long.parseLong(document.get("date"));
            json.put(field,DateUtils.format(new Date(times)));
        }else {
            JSONObject object = LuceneFormatter.convertObject(field,document);
            json.putAll(object);
            //json.put(formatter.field,document.get(formatter.field));
        }
        return json;
    }


}
