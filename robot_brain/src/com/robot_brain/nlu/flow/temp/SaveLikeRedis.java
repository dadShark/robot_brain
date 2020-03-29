package com.robot_brain.nlu.flow.temp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.robot_brain.nlu.flow.bean.FlowRunningHistory;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * @ClassName SaveLikeRedis
 * @Description 模拟redis中的存储结构，用于开发调试
 * @Author shark
 * @Date 2020/3/29 13:30
 * @Version V1.0
 **/
public class SaveLikeRedis {
    public static Map<String, String> flowName2ID = new HashMap<>();
    public static Map<String, HashMap<String, String>> nodeRules = new HashMap<>();
    public static Map<String, LinkedList<FlowRunningHistory>> flowRunningHistory=new HashMap<>();

    public SaveLikeRedis() {
        HashMap<String, String> rules = new HashMap<>();
        flowName2ID.put("入口：询问用户类型", "1.1");
        rules.put("1", "[\"用户类型=\\\"\\\"  =>  SET(\\\"TTS\\\",\\\"请问您是个人用户还是单位用户？\\\");NEXT(\\\"break\\\")\",\"用户类型!=\\\"\\\" => NEXT(\\\"2\\\")\"]");
        rules.put("2", "[\"用户类型=\\\"个人\\\" => NEXT(\\\"3\\\")\",\"用户类型=\\\"单位\\\" => NEXT(\\\"6\\\")\"]");
        rules.put("3", "[\"开户姓名=\\\"\\\" and 身份证号=\\\"\\\" => SET(\\\"TTS\\\",\\\"请问您的开户姓名和身份证号\\\");NEXT(\\\"break\\\")\",\"\u200B开户姓名=\\\"存在\\\" and 身份证号=\\\"\\\" => SET(\\\"TTS\\\",\\\"请问您的身份证号\\\");NEXT(\\\"break\\\")\",\"\u200B开户姓名=\\\"\\\" and 身份证号=\\\"存在\\\" => SET(\\\"TTS\\\",\\\"请问您的开户姓名\\\");NEXT(\\\"break\\\")\u200B\",\"开户姓名=\\\"存在\\\" and 身份证号=\\\"存在\\\" =>NEXT(\\\"4\\\")\u200B\"]");
        rules.put("4", "[\"开户姓名=\\\"存在\\\" and 身份证号=\\\"存在\\\" =>信息获取(\\\"查询个人名下车牌号\\\");NEXT(\\\"5\\\")\"]");
        rules.put("5", "[\"TRUE => NEXT_FLOW(\\\"查询通行明细\\\")\"]");
        rules.put("6", "[\"单位证件号=\\\"\\\" =>SET(\\\"TTS\\\",\\\"请输入您办理ETC时单位预留的证件号\\\");NEXT(\\\"break\\\")\",\"单位证件号=\\\"存在\\\" =>\u200BNEXT(\\\"7\\\")\"]");
        rules.put("7", "[\"单位名下车牌号=\\\"\\\" => 信息获取(\\\"查询单位名下车牌号\\\");NEXT(\\\"8\\\")\"]");
        rules.put("8", "[\"TRUE => NEXT_FLOW(\\\"查询通行明细\\\")\"]");
        nodeRules.put("1.1", rules);
        flowName2ID.put("查询通行明细", "1.2");
        rules.clear();
        rules.put("1", "[\"名下车牌号数量=0 => NEXT(\\\"2\\\")\",\"名下车牌号数量>10 => NEXT(\\\"3\\\")\",\"名下车牌号数量<=10 => NEXT(\\\"4\\\")\"]");
        rules.put("2", "[\"名下车牌号数量=0 =>SET(\\\"TTS\\\",\\\"名下暂无车辆信息，请核实正确的证件号、姓名\\\");挂机();NEXT(break)\"]");
        rules.put("3", "[\"名下车牌号数量>10 =>SET(\\\"TTS\\\",\\\"正在为您转接人工客服\\\");NEXT(break)\"]");
        rules.put("4", "[\"车牌号=\\\"\\\" =>菜单选项(\\\"请选择其中一个车牌号<@选项文本>\\\",\\\"<@名下所有车牌>\\\");NEXT(\\\"break\\\")\u200B\",\"车牌号=\\\"存在\\\" => NEXT(\\\"5\\\")\u200B\"]");
        rules.put("5", "[\"车牌号=\\\"存在\\\" => 信息获取(\\\"查询车牌号名下的ETC卡\\\");NEXT(\\\"6\\\")\"]");
        rules.put("6", "[\"ETC卡号数量>1 =>NEXT(\\\"7\\\")\",\"ETC卡号数量=1 =>NEXT(\\\"8\\\")\u200B\"]");
        rules.put("7", "[\"ETC卡号数量>1 =>SET(\\\"很抱歉，由于您车牌******下有多张ETC卡，暂无法为您查询，现为您转接人工\\\");NEXT(break)\"]");
        rules.put("8", "[\"确认ETC卡号=“” => SET(\\\"TTS\\\",\\\"您的ETC卡号是<@ETC卡号>,请核实卡号是否正确);NEXT(\\\"break\\\")\",\"\u200B确认ETC卡号=\\\"存在\\\" => NEXT(\\\"9\\\")\"]");
        rules.put("9", "[\"确认ETC卡号=\\\"否\\\" =>NEXT(\\\"10\\\")\",\"\u200B确认ETC卡号=\\\"是\\\" =>NEXT(\\\"11\\\")\"]");
        rules.put("10", "[\"确认ETC卡号=\\\"否\\\"  =>SET(\\\"TTS\\\",\\\"正在为您转接人工客服\\\");NEXT(\\\"break\\\")\"]");
        rules.put("11", "[\"查询日期=\\\"\\\" => SET(\\\"TTS\\\",\\\"您好，目前可为您查询一年以内的通行明细，请您输入需要查询的日期，例如：20190305-20190308\\\");NEXT(\\\"break\\\")\\n\u200B查询日期=\\\"存在\\\" => NEXT(\\\"12\\\")\"]");
        rules.put("12", "[\"通行明细=\\\"\\\" => 信息获取(\\\"查询通行明细\\\");NEXT(\\\"13\\\")\"]");
        rules.put("13", "[\"通行明细!=\\\"\\\" =>SET(\\\"TTS\\\",\\\"<@通行明细>\\\");NEXT(\\\"break\\\")\"]");
        nodeRules.put("1.2", rules);
    }

}
