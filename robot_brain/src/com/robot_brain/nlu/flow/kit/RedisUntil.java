package com.robot_brain.nlu.flow.kit;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.Map;

public class RedisUntil {

    private volatile  static JedisPool jedisPool = null;
    private static String redisConfigFile = "redis.xml";
    //把redis连接对象放到本地线程中
    private static ThreadLocal<Jedis> local=new ThreadLocal<Jedis>();
    private RedisUntil() {
    }

    //redis操作类
    /*初始化连接池
     *@author:waterkingko
     *@date:2020年3月30日 23:29:03
     */
    public static void initRedisPool(){
        try{
            JedisPoolConfig config =new JedisPoolConfig();
            //设置config属性
            config.setMaxTotal(Integer.valueOf(GenericUntil.getGlobalProfileInfo("redisMaxTotal")));
            config.setMaxIdle(Integer.valueOf(GenericUntil.getGlobalProfileInfo("redisMaxIdle")));
            config.setMaxWaitMillis(Integer.valueOf(GenericUntil.getGlobalProfileInfo("redisMaxWaitMillis")));
            String redisIp =GenericUntil.getGlobalProfileInfo("redisIp");
            int redisPort =Integer.valueOf(GenericUntil.getGlobalProfileInfo("redisPort"));
            int redisTimeout =Integer.valueOf(GenericUntil.getGlobalProfileInfo("redisTimeout"));
            String redisPwd ="".equals(GenericUntil.getGlobalProfileInfo("redisPwd"))?null:
                    GenericUntil.getGlobalProfileInfo("redisPwd");
            //config.setMaxTotal(Integer.valueOf(GenericUntil.getGlobalProfileInfo("redisMaxTotal")));
            jedisPool = new JedisPool(config,redisIp,redisPort,redisTimeout,redisPwd);
            System.out.println("连接池初始化成功");
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("连接池初始化失败");
        }
    }
    //获取jedis连接
    public static Jedis getCon(){
        Jedis jedis =local.get();
        if (jedis == null){
            if (jedisPool==null){
                synchronized(RedisUntil.class){
                    if(jedisPool == null){
                        initRedisPool();
                    }
                }
            }
            try{
                jedis = jedisPool.getResource();
            }catch (JedisConnectionException e){
                e.printStackTrace();
            }
            local.set(jedis);
        }
        return jedis;

    }
    //关闭jedis链接
    public static void closeCon(){
        Jedis jedis = local.get();
        if(jedis != null){
            jedis.close();
        }
        local.set(null);
    }
    //删除key
    public static void del(String key){
        Jedis jedis = RedisUntil.getCon();
        if (jedis ==null){
            return;
        }
        jedis.del(key.getBytes());
        RedisUntil.closeCon();
    }

    //jedis存入map
    public static void setMap2Reids(String key, Map<String, String> map) throws Exception{
        Jedis getjedis = RedisUntil.getCon();
        if(getjedis ==null){
            return;
        }
        getjedis.hmset(key, map);
        RedisUntil.closeCon();
    }
    //jedis取Map
    public static Map getMap4Redis(String key) throws Exception{
        Map maps = null ;
        Jedis getjedis =RedisUntil.getCon();
        if (getjedis == null){
            return maps;
        }
        maps = getjedis.hgetAll(key);
        return maps;
    }
    //jedis存第三方接口
    public static void setOutSideApiReids(Map<String, String> map) throws Exception {
        String key ="config:outsidApiInfo";
        RedisUntil.del(key);
        RedisUntil.setMap2Reids(key,map);
    }


    public static Map<String, String> getOutSideApiRedis(String key) {
        Map maps = null;
        try {
            maps = RedisUntil.getMap4Redis(key);

        }catch (Exception e){
            e.printStackTrace();
            System.out.println("读取第三方接口数据失败");
        }
        return maps;
    }
}
