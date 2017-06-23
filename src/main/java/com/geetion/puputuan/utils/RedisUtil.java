package com.geetion.puputuan.utils;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;

/**
 * Created by guodikai on 2017/1/4.
 */
@Component
public class RedisUtil {

    @Resource(name = "redisTemplate")
    private RedisTemplate<Serializable, Serializable> redisTemplate;


    public RedisTemplate getRedisTemplate(){
        return redisTemplate;
    }
    /**
     * <p>通过key获取储存在redis中的value</p>
     * <p>并释放连接</p>
     * @param key
     * @return 成功返回value 失败返回null
     */
    public String get(String key){
        return redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection)
                    throws DataAccessException {
                return deserialize(connection.get(serialize(key)));
            }
        });
    }

    /**
     * <p>向redis存入key和value,并释放连接资源</p>
     * <p>如果key已经存在 则覆盖</p>
     * @param key
     * @param value
     * @return 成功 返回OK 失败返回 0
     */
    public void set(String key,String value){
        redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection)
                    throws DataAccessException {

                byte[] keys = serialize(key);
                byte[] values = serialize(value);

                connection.set(keys, values);
                return true;
            }
        });
    }

    /**
     * <p>删除指定的key,也可以传入一个包含key的数组</p>
     * @param keys 一个key  也可以使 string 数组
     * @return 返回删除成功的个数
     */
    public Long del(String...keys){
        return redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection)
                    throws DataAccessException {

                Long count = 0L;

                for (int i = 0; i < keys.length; i++){
                    Long del = connection.del(serialize(keys[i]));
                    count = count + del;
                }
                return count;
            }
        });
    }

    /**
     * <p>判断key是否存在</p>
     * @param key
     * @return true OR false
     */
    public Boolean exists(String key){
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection)
                    throws DataAccessException {

                byte[] keys = serialize(key);

                return connection.exists(keys);
            }
        });
    }

    /**
     * <p>设置key value,如果key已经存在则返回0,nx==> not exist</p>
     * @param key
     * @param value
     * @return 成功返回1 如果存在 和 发生异常 返回 0
     */
    public boolean setnx(String key ,String value){
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection)
                    throws DataAccessException {

                return connection.setNX(serialize(key), serialize(value));
            }
        });
    }

    /**
     * <p>设置key的值,并返回一个旧值</p>
     * @param key
     * @param value
     * @return 旧值 如果key不存在 则返回null
     */
    public String getset(String key,String value){
        return redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection)
                    throws DataAccessException {

                byte[] oldValue = connection.getSet(serialize(key), serialize(value));

                return redisTemplate.getStringSerializer().deserialize(oldValue);
            }
        });
    }

    /**
     * <p>通过key向list尾部添加字符串</p>
     * @param key
     * @param strs 可以使一个string 也可以使string数组
     * @return 返回list的value个数
     */
    public Long rpush(String key ,String...strs){
        return redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection)
                    throws DataAccessException {

                byte[] keys = serialize(key);
                byte[] serialize;
                Long count = 0L;
                for(int i = 0; i < strs.length; i++){
                    serialize = serialize(strs[i]);
                    count = connection.rPush(keys, serialize);
                }
                return count;
            }
        });
    }

    /**
     * <p>通过key从list的头部删除一个value,并返回该value</p>
     * @param key
     * @return
     */
    synchronized public String lpop(String key){
        return redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection)
                    throws DataAccessException {

                return deserialize(connection.lPop(serialize(key)));
            }
        });
    }

    /**
     * <p>通过key返回list的长度</p>
     * @param key
     * @return
     */
    public Long llen(String key){
        return redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection)
                    throws DataAccessException {
                if (exists(key)){
                    return connection.lLen(serialize(key));
                }else {
                    return 0L;
                }

            }
        });
    }

    public String lIndex(String key, Long index){
        return redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                byte[] value = connection.lIndex(redisTemplate.getStringSerializer().serialize(key), index);
                return null == value ? null : redisTemplate.getStringSerializer().deserialize(value);

            }
        });

    }

    /**
     * <p>通过key,存放对应的filed:value</p>
     * @param key
     * @param field
     * @param value
     */
    public void hset(String key, String field, String value) {
        redisTemplate.opsForHash().put(key, field, value);
    }

    /**
     * 通过key，取得field对应的value
     * @param key
     * @param field
     * @return
     */
    public String hget(String key,String field) {
        return (String)redisTemplate.opsForHash().get(key, field);
    }

    /**
     * 删除key对应的缓存
     * @param key
     */
    public void hdel(String key){
         redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection)
                    throws DataAccessException {

                return  connection.del(serialize(key));
            }
        });
    }

    /**
     * 取得key对应缓存的键值对应的键key
     * @param key
     * @return
     */
    public Set<Object> hkey(String key){
        return redisTemplate.opsForHash().keys(key);
    }

    /**
     * 通过key，存入相应的hash数据
     * @param key
     * @param hash
     */
    public void hmset(String key, Map<String, Object> hash){
        redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection)
                    throws DataAccessException {

                byte[] keys = redisTemplate.getStringSerializer().serialize(
                        key);

                Map<byte[], byte[]> map = new HashMap<byte[], byte[]>();

                for(String ks : hash.keySet()){
                    map.put(serialize(ks), serialize(hash.get(ks).toString()));
                }
                connection.hMSet(keys, map);

                return true;
            }
        });
    }


    /**
     * <p>通过key 获取指定的value 如果没有对应的value则返回null</p>
     * @param key
     * @return
     */
    public Map<Object, Object> hmget(String key){
        return redisTemplate.opsForHash().entries(key);
    }



    public void hmdel(String key){
        redisTemplate.opsForHash().delete(key);
    }

    /**
     * <p>通过key向指定的set中添加value</p>
     * @param key
     * @param members 可以是一个String 也可以是一个String数组
     * @return 添加成功的个数
     */
    public void sadd(String key,String...members){
        redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection)
                    throws DataAccessException {

                for (int i = 0; i < members.length; i++){
                    connection.sAdd(serialize(key), serialize(members[i]));
                }

                return true;
            }
        });
    }

    /**
     * <p>通过key取得set数据</p>
     * @param key
     * @return
     */
    public Set<String> sget(String key){
        Set<Serializable> members = redisTemplate.opsForSet().members(key);
        Set<String> set = new HashSet<>();
        for(Serializable s : members){
            set.add(s.toString());
        }
        return set;

    }

    /**
     * 通过key移除对应set数据
     * @param key
     */
    public void spop(String key){
        redisTemplate.opsForSet().pop(key);
    }

    /**
     * 通过key,移除set中对应的成员数据
     * @param key
     * @param member
     */
    public void srem(String key, String... member){
        redisTemplate.opsForSet().remove(key, member);
    }

    /**
     * 通过key，随机返回set中的一个值
     * @param key
     * @return
     */
    public String srand(String key){
        return (String) redisTemplate.opsForSet().randomMember(key);
    }
    private byte[] serialize(String s){
        return  redisTemplate.getStringSerializer().serialize(
                s);

    }

    private String deserialize(byte[] bytes){
        return  redisTemplate.getStringSerializer().deserialize(bytes);
    }

}
