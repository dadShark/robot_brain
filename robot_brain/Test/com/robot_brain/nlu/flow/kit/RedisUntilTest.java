package com.robot_brain.nlu.flow.kit;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class RedisUntilTest {

    @Test
    public void initRedisPool() {
        RedisUntil.initRedisPool();
    }

    @Test
    public void getCon() {
    }

    @Test
    public void closeCon() {
    }

    @Test
    public void del() {
    }

    @Test
    public void setMap2Reids() {
        Map<String,String> maps =new HashMap<String, String>(){
            {
                put("查询个人名下车牌号","map1");
                put("age","100");
            }
        };
    try {
        RedisUntil.setMap2Reids("new",maps);
    }catch (Exception e){
        System.out.println("存入出错"+e);
    }

    }

    @Test
    public void getMap4Redis() {
    }

    @Test
    public void setOutSideApiReids() {
    }

    @Test
    public void getOutSideApiRedis() {
    }
}