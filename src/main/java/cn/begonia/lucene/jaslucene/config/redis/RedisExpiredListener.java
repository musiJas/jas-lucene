package cn.begonia.lucene.jaslucene.config.redis;

import cn.begonia.lucene.jaslucene.util.CacheUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
public class RedisExpiredListener extends KeyExpirationEventMessageListener {
    /**
     * Creates new { MessageListener} for {@code __keyevent@*__:expired} messages.
     *
     * @param listenerContainer must not be {@literal null}.
     */
    @Autowired
    CacheUtils cacheUtils;


    public RedisExpiredListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }



    /**
     * 针对redis数据失效事件，进行数据处理
     * @param message
     * @param pattern
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString();
        expiredKey=expiredKey.substring(expiredKey.indexOf(":")+1);
        //String []  str=expiredKey.split(":");
        String  key =expiredKey.substring(0,expiredKey.indexOf(":"));
        String  field=expiredKey.substring(expiredKey.indexOf(":")+1);
        CacheUtils.delFromMap(key,field); //删除对应的map字段
    }

}
