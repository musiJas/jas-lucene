package cn.begonia.lucene.jaslucene.config;

import cn.begonia.lucene.jaslucene.common.CacheType;
import cn.begonia.lucene.jaslucene.resourece.ResourceAttribute;
import cn.begonia.lucene.jaslucene.service.handler.LuceneReaderService;
import cn.begonia.lucene.jaslucene.service.handler.LuceneWriterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.io.File;

@Slf4j
@Component
public class ServerStartup implements ApplicationContextAware, CommandLineRunner {
    private  static ApplicationContext applicationContext;

    @Override
    public void run(String... strings) throws Exception {
        log.info("ServerStartup run service......");
        //String proxy=ServerStartup.getApplicationContext().getEnvironment().getProperty("server.proxy");
        /** 项目启动就初始化inderWriter**/
        String index=applicationContext.getEnvironment().getProperty("begonia.lucene.indexPath");
        /*for(CacheType type:CacheType.values()){
            if(checkFile(index,type.getKey())){
                ResourceAttribute  rs= ResourceAttribute.builder().indexPath(index).category(type.getKey()).build();
                LuceneWriterService  service=applicationContext.getBean(LuceneWriterService.class);
                service.openResource(rs);
                LuceneReaderService   readerService=applicationContext.getBean(LuceneReaderService.class);
                readerService.openResource(rs);
            }
        }*/
    }

    public  static  boolean  checkFile(String  root,String  category){
        File  file=new File(root+File.separator+category);
        return  file.exists();
    }

    public static void main(String[] args) {
        String  root="D:\\data\\index";
        String  category="life";

        System.out.println(checkFile(root,category));


    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            this.applicationContext=applicationContext;
    }

    public static ApplicationContext getApplicationContext(){
        return   applicationContext;
    }

}
