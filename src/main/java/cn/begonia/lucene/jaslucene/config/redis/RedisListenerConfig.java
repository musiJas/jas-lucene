package cn.begonia.lucene.jaslucene.config.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
/**redis 失效监听器**/
@Configuration
public class RedisListenerConfig {

    @Bean
    public RedisMessageListenerContainer  initial(RedisConnectionFactory  factory){
        RedisMessageListenerContainer  container=new RedisMessageListenerContainer();
        container.setConnectionFactory(factory);
        return  container;
    }
   /* @Bean
    public KeyExpirationEventMessageListener redisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
        return new RedisExpiredListener(listenerContainer);
    }*/

}
