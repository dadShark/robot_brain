package com.robot_brain.nlu.flow.kit;

import redis.clients.jedis.Jedis;

public class RedisUntil {
    //redis操作类



    public static void main(String[] args) {
        Jedis jedis =new Jedis("localhost");
        System.out.println("连接redis成功");
        System.out.println("服务正在运行"+jedis.ping());
    }
}
