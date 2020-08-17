package cn.begonia.lucene.jaslucene;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@EnableConfigurationProperties
@SpringBootApplication
public class JasLuceneApplication {

    public static void main(String[] args) {
        SpringApplication.run(JasLuceneApplication.class, args);
    }

}
