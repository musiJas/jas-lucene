package cn.begonia.lucene.jaslucene.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author begonia_chen
 * @data 2020/8/17 11:14
 * @description
 **/
@Data
@Configuration
@ConfigurationProperties(prefix ="begonia.lucene")
public class ContextProperties {
    private  String  indexPath;

}
