package cn.begonia.lucene.jaslucene.config;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.io.*;
import java.math.BigInteger;

/**
 * @author begonia_chen
 * @data 2020/8/17 14:36
 * @description
 **/
public class ObjectRedisSerializer implements RedisSerializer<String> {

    @Override
    public byte[] serialize(String s) throws SerializationException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }

    @Override
    public String deserialize(byte[] bytes) throws SerializationException {
        try {
            ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new ByteArrayInputStream(bytes)));
            Object obj = ois.readObject();
            return  String.valueOf(obj);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
