package cn.begonia.lucene.jaslucene.famatter.formatter;

import cn.begonia.lucene.jaslucene.famatter.LuceneFormatter;
import cn.begonia.lucene.jaslucene.util.DateUtils;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.document.*;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;

import java.util.*;

public enum  CnblogsFormatter {
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
    img("img",TextField.class,Field.Store.YES, Field.Index.NOT_ANALYZED,1.0f);

    private  String  field;
    private  Class  fieldType;
    private  Field.Store  storeValue;
    private  Field.Index  indexValue;
    private  float    boost;
    CnblogsFormatter(){

    }


    CnblogsFormatter(String field, Class fieldType, Field.Store  storeValue, Field.Index indexValue, Float  boost){
        this.field=field;
        this.fieldType=fieldType;
        this.storeValue=storeValue;
        this.indexValue=indexValue;
        this.boost=boost;
    }


    public static  String[]  listFields(){
        List<String> list=new ArrayList<>();
        for(CnblogsFormatter matter:CnblogsFormatter.values()){
            list.add(matter.field);
        }
        return  list.toArray(new String[list.size()]);
    }

    @SuppressWarnings("all")
    public Map<String,Object> toMap() {
        Map<String,Object>  map = new HashMap<>();
        map.put("field",field);
        map.put("fieldType",fieldType);
        map.put("storeValue",storeValue);
        map.put("indexValue",indexValue);
        map.put("boost",boost);
        return map;
    }

    public static JSONObject  resolveDocument(Document  document){
          JSONObject json=new JSONObject();
         for(CnblogsFormatter  formatter:CnblogsFormatter.values()){
             // 对date做格式转换
            /* if(StringUtils.equals(formatter.field,"date")){
                 Long  times=Long.parseLong(document.get("date"));
                 json.put(formatter.field,DateUtils.format(new Date(times)));
             }else {
                 JSONObject object = LuceneFormatter.convertObject(formatter.field,document);
                 json.putAll(object);
                 //json.put(formatter.field,document.get(formatter.field));
             }*/
             json.putAll(LuceneFormatter.convertObject(formatter.field,document));
         }
        return json;
    }

    /** 获取排序规则 **/
    public  static Sort   getDefaultSort(){
        SortField  like=new SortField("like",SortField.Type.INT,true);
        SortField  date =new SortField("date",SortField.Type.LONG,true);
        Sort  sort=new Sort(date,like);
        return sort;
    }


    public static Field initialFormatter(String field, String value){
        for(CnblogsFormatter bf: CnblogsFormatter.values()){
            if(StringUtils.equals(field,bf.field)) {
               return LuceneFormatter.getField(field, value, bf.toMap(), bf.fieldType);
            }
        }
        return null;
    }

}
