package com.robot_brain.nlu.flow.temp;

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
    /**
     * {流程ID,{节点号，规则JSON}}
     */
    public static Map<String, HashMap<String, String>> nodeRules = new HashMap<>();
    /*
    {会话ID,有序的场景历史}
     */
    public static Map<String, LinkedList<FlowRunningHistory>> flowRunningHistory=new HashMap<>();

    public SaveLikeRedis() {
        HashMap<String, String> rules = new HashMap<>();
        flowName2ID.put("入口：询问用户类型", "1.1");
        rules.put("1", "[\"用户类型=\\\"\\\"  ==>  应答(\\\"请问您是个人用户还是单位用户？\\\");节点跳转(\\\"break\\\")\",\"用户类型!=\\\"\\\" ==> 节点跳转(\\\"2\\\")\"]");
        rules.put("2", "[\"用户类型=\\\"个人\\\" ==> 节点跳转(\\\"3\\\")\",\"用户类型=\\\"单位\\\" ==> 节点跳转(\\\"6\\\")\"]");
        rules.put("3", "[\"开户姓名=\\\"\\\" and 身份证号=\\\"\\\" ==> 应答(\\\"请问您的开户姓名和身份证号？\\\");节点跳转(\\\"break\\\")\",\"开户姓名=\\\"存在\\\" and 身份证号=\\\"\\\" ==> 应答(\\\"请问您的身份证号\\\");节点跳转(\\\"break\\\")\",\"开户姓名=\\\"\\\" and 身份证号=\\\"存在\\\" ==> 应答(\\\"请问您的开户姓名\\\");节点跳转(\\\"break\\\")\",\"开户姓名=\\\"存在\\\" and 身份证号=\\\"存在\\\" ==>节点跳转(\\\"4\\\")\"]");
        rules.put("4", "[\"开户姓名=\\\"存在\\\" and 身份证号=\\\"存在\\\" ==>信息获取(\\\"查询个人名下车牌号\\\");节点跳转(\\\"5\\\")\"]");
        rules.put("5", "[\"TRUE ==> 流程跳转(\\\"查询通行明细\\\")\"]");
        rules.put("6", "[\"单位证件号=\\\"\\\" ==>应答(\\\"请输入您办理ETC时单位预留的证件号\\\");节点跳转(\\\"break\\\")\",\"单位证件号=\\\"存在\\\" ==>节点跳转(\\\"7\\\")\"]");
        rules.put("7", "[\"单位名下车牌号=\\\"\\\" ==> 信息获取(\\\"查询单位名下车牌号\\\");节点跳转(\\\"8\\\")\"]");
        rules.put("8", "[\"TRUE ==> 流程跳转(\\\"查询通行明细\\\")\"]");
        nodeRules.put("1.1", rules);
        flowName2ID.put("查询通行明细", "1.2");
        HashMap<String, String> rules2 = new HashMap<>();
        rules2.put("1", "[\"名下车牌号数量=0 ==> 节点跳转(\\\"2\\\")\",\"名下车牌号数量>10 ==> 节点跳转(\\\"3\\\")\",\"名下车牌号数量<=10 ==> 节点跳转(\\\"4\\\")\"]");
        rules2.put("2", "[\"名下车牌号数量=0 ==>应答(\\\"名下暂无车辆信息，请核实正确的证件号、姓名\\\");结束会话();节点跳转(break)\"]");
        rules2.put("3", "[\"名下车牌号数量>10 ==>应答(\\\"正在为您转接人工客服\\\");节点跳转(\\\"break\\\")\"]");
        rules2.put("4", "[\"车牌号=\\\"\\\" ==>提供选单(\\\"请选择其中一个车牌号<@选项文本>\\\",\\\"<@名下所有车牌号>\\\");节点跳转(\\\"break\\\")\",\"车牌号=\\\"存在\\\" ==> 节点跳转(\\\"5\\\")\"]");
        rules2.put("5", "[\"车牌号=\\\"存在\\\" ==> 信息获取(\\\"查询车牌对应的ETC卡号\\\");节点跳转(\\\"6\\\")\"]");
        rules2.put("6", "[\"ETC卡号数量>1 ==>节点跳转(\\\"7\\\")\",\"ETC卡号数量=1 ==>节点跳转(\\\"8\\\")\"]");
        rules2.put("7", "[\"ETC卡号数量>1 ==>SET(\\\"很抱歉，由于您车牌******下有多张ETC卡，暂无法为您查询，现为您转接人工\\\");节点跳转(break)\"]");
        rules2.put("8", "[\"确认ETC卡号=\\\"\\\" ==> 应答(\\\"您的ETC卡号是<@ETC卡号>,请核实卡号是否正确);节点跳转(\\\"break\\\")\",\"确认ETC卡号=\\\"存在\\\" ==> 节点跳转(\\\"9\\\")\"]");
        rules2.put("9", "[\"确认ETC卡号=\\\"否\\\" ==>节点跳转(\\\"10\\\")\",\"确认ETC卡号=\\\"是\\\" ==>节点跳转(\\\"11\\\")\"]");
        rules2.put("10", "[\"确认ETC卡号=\\\"否\\\"  ==>应答(\\\"正在为您转接人工客服\\\");节点跳转(\\\"break\\\")\"]");
        rules2.put("11", "[\"查询日期=\\\"\\\" ==> 应答(\\\"您好，目前可为您查询一年以内的通行明细，请您输入需要查询的日期，例如：20190305-20190308\\\");节点跳转(\\\"break\\\")\",\"查询日期=\\\"存在\\\" ==> 节点跳转(\\\"12\\\")\"]");
        rules2.put("12", "[\"通行明细=\\\"\\\" ==> 信息获取(\\\"查询通行明细\\\");节点跳转(\\\"13\\\")\"]");
        rules2.put("13", "[\"通行明细!=\\\"\\\" ==>应答(\\\"<@通行明细>\\\");结束流程();节点跳转(\\\"break\\\")\"]");
        nodeRules.put("1.2", rules2);
    }

}
