package com.robot_brain.nlu.flow.kit;

import net.sf.json.JSON;
import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;

import java.util.Map;

public class RedisUntil {
    //redis操作类

    public static void jedisCon(){
        String jedishost =GenericUntil.getGlobalProfileInfo("redisurl");
        Jedis jedis =new Jedis(jedishost);
    }

    public static void setReids(Map<String, JSON> map){


    }


    public static void setOutSideApiReids(Map<String, JSONObject> map) {
        jedisCon();

    }

    public static Map<String, String> getOutSideApiRedis(String key) {


        return null;
    }
}
