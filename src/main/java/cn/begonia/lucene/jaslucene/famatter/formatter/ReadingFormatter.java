package cn.begonia.lucene.jaslucene.famatter.formatter;

import cn.begonia.lucene.jaslucene.util.DateUtils;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.document.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public enum ReadingFormatter {
 /*   Field.Index.ANALYZED:分词建索引
    Field.Index.ANALYZED_NO_NORMS:分词建索引，但是Field的值不像通常那样被保存，而是只取一个byte，这样节约存储空间(ANALYZED存储了index time,boost information等norms，而ANALYZED_NO_NORMS不存储)
    Field.Index.NOT_ANALYZED:不分词且索引,即不使用 analyzer分析，整体作为一个token，常用语精确匹配，例如文件名，ID号等就用这个
    Field.Index.NOT_ANALYZED_NO_NORMS:不分词建索引，Field的值去一个byte保存*/

    title("title", TextField.class, Field.Store.YES,Field.Index.ANALYZED,5.0f),
    date("date", LongField.class,Field.Store.YES,Field.Index.ANALYZED,1.0f),
    img("img",TextField.class,Field.Store.YES, Field.Index.NOT_ANALYZED,1.0f),
    score("score",StringField.class,Field.Store.YES, Field.Index.NOT_ANALYZED,1.0f),
    info("info",TextField.class,Field.Store.YES, Field.Index.ANALYZED_NO_NORMS,1.0f),
    author("author",TextField.class,Field.Store.YES, Field.Index.NOT_ANALYZED,1.0f),
    detail("detail",TextField.class,Field.Store.YES, Field.Index.NOT_ANALYZED,1.0f),
    type("type",TextField.class,Field.Store.YES, Field.Index.NOT_ANALYZED,1.0f);



    private  String  field;
    private  Class  fieldType;
    private  Field.Store  storeValue;
    private  Field.Index  indexValue;
    private  float    boost;
    ReadingFormatter(){

    }


    ReadingFormatter(String field, Class fieldType, Field.Store  storeValue, Field.Index indexValue, Float  boost){
        this.field=field;
        this.fieldType=fieldType;
        this.storeValue=storeValue;
        this.indexValue=indexValue;
        this.boost=boost;
    }


    public static  String[]  listFields(){
        List<String> list=new ArrayList<>();
        for(ReadingFormatter matter:ReadingFormatter.values()){
            list.add(matter.field);
        }
        return  list.toArray(new String[list.size()]);
    }


    public static JSONObject resolveDocument(Document  document){
        JSONObject json=new JSONObject();
        for(ReadingFormatter  formatter:ReadingFormatter.values()){
            // 对date做格式转换
            if(StringUtils.equals(formatter.field,"date")){
                Long  times=Long.parseLong(document.get("date"));
                json.put(formatter.field,DateUtils.format(new Date(times)));
            }else {
                json.put(formatter.field,document.get(formatter.field));
            }
        }
        return json;
    }

    public static Field initialFormatter(String field, String value){
        /*Field  field1=new TextField("","",null);
        field1.setBoost(5.0f);*/
        for(ReadingFormatter bf: ReadingFormatter.values()){
            if(StringUtils.equals(field,bf.field)){
                try {
                    Constructor<Field> constructor=null;
                    Field fs=null;
                    if(StringUtils.equals(bf.fieldType.getName(), IntField.class.getName())){
                        constructor=bf.fieldType.getConstructor(String.class,int.class, Field.Store.class);
                        fs= constructor.newInstance(field,Integer.parseInt(value),bf.storeValue);
                    }else if(StringUtils.equals(bf.fieldType.getName(), LongField.class.getName())){
                        constructor=bf.fieldType.getConstructor(String.class,long.class, Field.Store.class);
                        fs= constructor.newInstance(field, DateUtils.parse(value,"yyyy-MM-dd HH:mm").getTime(),bf.storeValue);
                    }else {
                        constructor=bf.fieldType.getConstructor(String.class,String.class, Field.Store.class);
                        fs= constructor.newInstance(field,value,bf.storeValue);
                        fs.setBoost(bf.boost);// 更新权重,默认是1.0f
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

            }
        }
        return null;
    }

}
