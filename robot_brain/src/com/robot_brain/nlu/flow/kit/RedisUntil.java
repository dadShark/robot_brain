package com.robot_brain.nlu.flow.kit;

import redis.clients.jedis.Jedis;

import java.util.Map;

public class RedisUntil {
    //redis操作类



    public static void setReids(String busniess, String apps, String names, Map maps){


    }

    public static void main(String[] args) {
        Jedis jedis =new Jedis("localhost");
        System.out.println("连接redis成功");
        System.out.println("服务正在运行"+jedis.ping());
    }

    public static void setOutSideApiReids(String mbusiness, String mApp, String mname, Map<String, String> row) {

    }

    public static Map<String, String> getOutSideApiRedis(String business, String apps, String names) {
        Map<String,String> maps =null;
        return maps;
    }
}
