package com.wiselong.tool.im.cache;

import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class RedissonCache {

        @Autowired
        private RedissonClient redissonClient;

        public RedissonClient getRedissonClient() {
            return redissonClient;
        }



    /**
         * 缓存基本的对象，Integer、String、实体类等
         *
         * @param key 缓存的键值
         * @param value 缓存的值
         */
        public <T> void setCacheObject(final String key, final T value)
        {
            RBucket<T> bucket = redissonClient.getBucket(key);
            bucket.set(value);
        }

        /**
         * 缓存基本的对象，Integer、String、实体类等
         *
         * @param key 缓存的键值
         * @param value 缓存的值
         * @param timeout 时间
         * @param timeUnit 时间颗粒度
         */
        public <T> void setCacheObject(final String key, final T value, final Integer timeout, final TimeUnit timeUnit)
        {
            RBucket<T> bucket = redissonClient.getBucket(key);
            bucket.set(value,timeout,timeUnit);
        }

        /**
         * 设置有效时间
         *
         * @param key Redis键
         * @param timeout 超时时间
         * @return true=设置成功；false=设置失败
         */
        public boolean expire(final String key, final long timeout)
        {
            return redissonClient.getBucket(key).expire(timeout, TimeUnit.SECONDS);
        }

        /**
         * 设置有效时间
         *
         * @param key Redis键
         * @param timeout 超时时间
         * @param unit 时间单位
         * @return true=设置成功；false=设置失败
         */
        public boolean expire(final String key, final long timeout, final TimeUnit unit)
        {
            return redissonClient.getBucket(key).expire(timeout,unit);
        }

        /**
         * 获得缓存的基本对象。
         *
         * @param key 缓存键值
         * @return 缓存键值对应的数据
         */
        public <T> T getCacheObject(final String key)
        {
            RBucket<T> bucket = redissonClient.getBucket(key);
            return bucket.get();
        }

        /**
         * 删除单个对象
         *
         * @param key
         */
        public boolean deleteObject(final String key)
        {
            return redissonClient.getBucket(key).delete();
        }

        /**
         * 缓存List数据
         *
         * @param key 缓存的键值
         * @param dataList 待缓存的List数据
         * @return 缓存的对象
         */
        public <T> long setCacheList(final String key, final List<T> dataList,Integer seconds)
        {
            RList<T> rList = redissonClient.getList(key);
            boolean isSuccess = rList.expire(seconds,TimeUnit.SECONDS);
            if(isSuccess){
                isSuccess =  rList.addAll(dataList);
                if(isSuccess){
                    return dataList.size();
                }
            }
            return 0l;
        }

        /**
         * 缓存List数据
         *
         * @param key 缓存的键值
         * @param dataList 待缓存的List数据
         * @return 缓存的对象
         */
        public <T> long setCacheList(final String key, final List<T> dataList)
        {
            RList<T> rList = redissonClient.getList(key);
            rList.expire(120,TimeUnit.SECONDS);
            boolean isSuccess =  rList.addAll(dataList);
            if(isSuccess){
                return dataList.size();
            }
            return 0l;
        }

        /**
         * 获得缓存的list对象
         *
         * @param key 缓存的键值
         * @return 缓存键值对应的数据
         */
        public <T> List<T> getCacheList(final String key)
        {
            RList<T>  rList = redissonClient.getList(key);
            return rList.readAll();
        }

        /**
         * 缓存Set
         *
         * @param key 缓存键值
         * @param dataSet 缓存的数据
         * @return 缓存数据的对象
         */
        public <T> long setCacheSet(final String key, final Set<T> dataSet)
        {
            RSet<T> rset = redissonClient.getSet(key);
            boolean isSuccess = rset.addAll(dataSet);
            return isSuccess ? dataSet.size():0;
        }

        /**
         * 获得缓存的set
         *
         * @param key
         * @return
         */
        public <T> Set<T> getCacheSet(final String key)
        {
            RSet<T> rset = redissonClient.getSet(key);
            return rset.readAll();
        }

        /**
         * 缓存Map
         *
         * @param key
         * @param dataMap
         */
        public <T> void setCacheMap(final String key, final Map<String, T> dataMap)
        {
            if (dataMap != null) {
                RMap<String,T> rmap =  redissonClient.getMap(key);
                rmap.putAll(dataMap);
            }
        }

        /**
         * 获得缓存的Map
         *
         * @param key
         * @return
         */
        public <T> Map<String, T> getCacheMap(final String key)
        {
            RMap<String,T> rmap =  redissonClient.getMap(key);
            return rmap.readAllMap();
        }

    /**
     * 获得缓存的Map
     *
     * @param key
     * @return
     */
    public  boolean   cacheMapExists(final String key,String hkey)
    {
       return  redissonClient.getMap(key).containsKey(hkey);
    }

        /**
         * 获得缓存的Map
         *
         * @param key
         * @return
         */
        public void clearCacheMap(final String key)
        {
           redissonClient.getMap(key).clear();
        }
        /**
         * 往Hash中存入数据
         *
         * @param key Redis键
         * @param hKey Hash键
         * @param value 值
         */
        public <T> void setCacheMapValue(final String key, final String hKey, final T value)
        {
            RMap<String,T> rmap =  redissonClient.getMap(key);
            rmap.put(hKey,value);
        }


        /**
         * 获取Hash中的数据
         *
         * @param key Redis键
         * @param hKey Hash键
         * @return Hash中的对象
         */
        public <T> T getCacheMapValue(final String key, final String hKey)
        {
            RMap<String,T> rmap =  redissonClient.getMap(key);
            return rmap.get(hKey);
        }

        /**
         * 获得缓存的基本对象列表
         *
         * @param pattern 字符串前缀
         * @return 对象列表
         */
        public Collection<String> keys(final String pattern)
        {
            RKeys rkeys =  redissonClient.getKeys();
            Iterable<String> keys =   rkeys.getKeysByPattern(pattern);
            List<String> list=new ArrayList<String>();
            if(keys!=null){
                keys.forEach(key->list.add(key));
            }
            return list;
        }

        /**
         * 获得缓存的基本对象列表
         *
         * @param pattern 字符串前缀
         * @return 对象列表
         */
        public void delkeys(final String pattern)
        {
            RKeys rkeys =  redissonClient.getKeys();
            rkeys.deleteByPattern(pattern);
        }

}
