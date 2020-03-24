package com.robot_brain.nlu.flow.kit;

import net.sf.json.JSON;
import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;

import java.util.Map;

public class RedisUntil {
    //redis操作类



    public static void setReids(Map<String, JSON> map){


    }

    public static void main(String[] args) {
        Jedis jedis =new Jedis("localhost");
        System.out.println("连接redis成功");
        System.out.println("服务正在运行"+jedis.ping());
    }

    public static void setOutSideApiReids(Map<String, JSONObject> map) {

    }

    public static Map<String, String> getOutSideApiRedis(String key) {


        return null;
    }
}
