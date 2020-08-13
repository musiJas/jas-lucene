package cn.begonia.lucene.jaslucene.resourece;

import cn.begonia.lucene.jaslucene.common.ResourceType;
import lombok.Data;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;

import java.io.IOException;

/**
 *  资源 抽象类
 *  资源类的集合
 *
 * **/
@Data
public  class  ResourceAttribute {
    private ResourceType type; // 转换资源的类型
    private IndexWriter  writer; //  lucene  写入索引类
    private IndexReader  reader; // lucene  读入索引类
    private  String  indexPath;  //索引位置

    public  void  closeHandler(IndexWriter  writer){
        if(writer!=null){
            try {
                writer.commit();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public  void   closeWriter(IndexWriter writer){
        if(writer!=null){
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
