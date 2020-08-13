package cn.begonia.lucene.jaslucene.resourece;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@SuppressWarnings("all")
public class SimtextResource extends ResourceAttribute {
    private  String  field;  //创建索引的field
    private  String  content; // 创建索引的内容
    private  boolean  isMultiResult=false; // 是否是复合文件创建
    private  List<Map<String,Object>>   list; //文本创建所以的内容

}
