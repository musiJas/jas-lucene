package cn.begonia.lucene.jaslucene.resourece;

import cn.begonia.lucene.jaslucene.common.ResourceType;
import lombok.Data;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;

@Data
public class FileResource extends   ResourceAttribute{
    private  String  resourcePath; //文件资源路径

    public  FileResource(){

    }

    public FileResource(ResourceType type, IndexWriter writer, IndexReader reader, String indexPath, String category, String resourcePath) {
        super(type, writer, reader, indexPath, category);
        this.resourcePath = resourcePath;
    }
}
