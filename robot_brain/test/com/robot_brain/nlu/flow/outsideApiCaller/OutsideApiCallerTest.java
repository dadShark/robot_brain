package com.robot_brain.nlu.flow.outsideApiCaller;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class OutsideApiCallerTest {

    @Test
    public void _init_() {
    }

    @Test
    public void _main_() {
        Map maps=new HashMap<String,String>();
        maps.put("ApplicationID","10001");
        maps.put("InterfaceName","查询个人名下车牌号");
        maps.put("funcation","3");
        maps.put("ETC卡号","ETC001");
        maps.put("日期","2020年4月6日");
        maps.put("信息获取","查询个人名下车牌号");
        maps.put("商家ID","10001");
        OutsideApiCaller outsideApiCaller =new OutsideApiCaller();
        System.out.println(outsideApiCaller._main_(maps));
    }
}