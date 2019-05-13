package top.imlgw.spike.redis;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.security.Key;

/**
 * @author imlgw.top
 * @date 2019/5/11 22:03
 */
@Service
public class RedisService {

    @Autowired
    private JedisPool jedisPool;

    public <T> T get(KeyPrefix prefix,String key, Class<T> clz) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey=prefix.getPrefix()+key;
            String res = jedis.get(realKey);
            return stringToBean(res,clz);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public <T> boolean set(KeyPrefix prefix,String key, T value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey=prefix.getPrefix()+key;
            String res = jedis.set(realKey, beanToString(value));
            return true;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * @param prefix
     * @param key
     * @param <T>
     * @return 是否存在
     */
    public <T>boolean exist(KeyPrefix prefix,String key){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey=prefix.getPrefix()+key;
            Boolean exists = jedis.exists(realKey);
            return exists;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * @param prefix
     * @param key
     * @param <T>
     * @return 自减后的值
     */
    public <T>Long decr(KeyPrefix prefix,String key){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey=prefix.getPrefix()+key;
            return jedis.decr(realKey);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }


    /**
     * @param prefix
     * @param key
     * @param <T>
     * @return 自增后的值
     */
    public <T>Long incr(KeyPrefix prefix,String key){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey=prefix.getPrefix()+key;
            return jedis.incr(realKey);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }


    private <T> String beanToString(T value) {
        if(value==null){
            return null;
        }
        Class<?> clz = value.getClass();
        if(clz ==int.class||clz ==Integer.class){
            return  ""+value;
        }else if(clz == String.class){
            return value+"";
        }else if(clz == long.class || clz== Long.class){
            return ""+value;
        }else {
            return JSON.toJSONString(value);
        }
    }


    private <T> T stringToBean(String value,Class clz) {
        if(value==null||value.length()<=0||clz==null){
            return null;
        }
        if(clz ==int.class||clz ==Integer.class){
            return (T) Integer.valueOf(value);
        }else if(clz == String.class){
            return (T) value;
        }else if(clz == long.class || clz== Long.class){
            return (T) Long.valueOf(value);
        }else {
            return (T) JSON.toJavaObject(JSON.parseObject(value),clz);
        }
    }
}
