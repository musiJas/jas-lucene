package cn.begonia.lucene.jaslucene.service.impl;

import cn.begonia.lucene.jaslucene.resourece.FileResource;
import cn.begonia.lucene.jaslucene.resourece.ResourceAttribute;
import cn.begonia.lucene.jaslucene.service.DocumentConvert;
import cn.begonia.lucene.jaslucene.util.FileUtil;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

/**
 * @author begonia_chen
 * @data 2020/8/13 15:28
 * @description  针对文件生成索引数据
 **/
@SuppressWarnings("all")
@Service
public class FileConvertServiceImpl implements DocumentConvert {
    @Override
    public void  convertHandlerDocument(ResourceAttribute attribute) {
        IndexWriter indexWriter = attribute.getWriter();
        FileResource fileResource = (FileResource) attribute;
        File file = new File(fileResource.getResourcePath());
        if (file.isDirectory()) {
            try {
                for (File fl : file.listFiles()) {
                    Document doc = new Document();
                    String fileName = fl.getName();
                    Field fieldName = new StringField("fileName", fileName, Field.Store.YES);
                    long fileSize = fl.length();
                    Field fieldSize = new StringField("fileSize", String.valueOf(fileSize), Field.Store.YES);
                    String filePath = fl.getPath();
                    Field fieldPath = new StoredField("filePath", filePath);
                    String fileContent = FileUtil.getStringByFile(fl);
                    Field fieldContent = new TextField("fileContent", fileContent, Field.Store.YES);
                    doc.add(fieldName);
                    doc.add(fieldSize);
                    doc.add(fieldPath);
                    doc.add(fieldContent);
                    indexWriter.addDocument(doc);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                indexWriter.commit();
                indexWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
