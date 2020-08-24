package cn.begonia.lucene.jaslucene.config;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;

/**
 * @author begonia_chen
 * @data 2020/8/24 16:23
 * @description
 **/
public class  IOIndexConfig {

    private  static  Object reader=new Object();
    private  static  Object writer=new Object();
    private  static  IndexSearcher  indexSearcher=null;
    private  static  DirectoryReader  directoryReader=null;
    private  static IndexWriter  indexWriter=null;


    public  static  IndexSearcher  getIndexSearcherInstance(String indexPath,String category){
        FSDirectory  fsDirectory;
        if(indexSearcher==null){
                synchronized (reader){
                    if(indexSearcher!=null){
                        File businessFile=new File(indexPath+ File.separator+category);
                        try {
                            fsDirectory= FSDirectory.open(businessFile);
                            directoryReader= DirectoryReader.open(fsDirectory);
                            indexSearcher=new IndexSearcher(directoryReader);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
        }
        return indexSearcher;
    }

    public  static  IndexWriter  getIndexWriterInstance(String  indexPath){
        FSDirectory  fsDirectory;
        if(indexWriter!=null){
            synchronized (writer){
                if(indexWriter!=null){
                    try {
                        fsDirectory= FSDirectory.open(new File(indexPath));
                        IKAnalyzer ikAnalyzer=new IKAnalyzer();
                        IndexWriterConfig indexWriterConfig=new IndexWriterConfig(Version.LUCENE_CURRENT,ikAnalyzer);
                        indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
                        indexWriter =new IndexWriter(fsDirectory,indexWriterConfig);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return indexWriter;
    }


}
