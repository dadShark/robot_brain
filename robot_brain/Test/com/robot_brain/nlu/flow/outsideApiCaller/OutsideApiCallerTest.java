package com.robot_brain.nlu.flow.outsideApiCaller;

import com.robot_brain.nlu.flow.bean.OutsideApiInfo;
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
        maps.put("InterfaceName","查询通行明细");
        maps.put("funcation","3");
        maps.put("ETC卡号","ETC001");
        maps.put("日期","2020年4月6日");
        OutsideApiCaller outsideApiCaller =new OutsideApiCaller();
        System.out.println(outsideApiCaller._main_(maps));
    }
}