package com.robot_brain.nlu.flow.Verbs;

import com.robot_brain.nlu.communal.kit.ProcessLog;
import com.robot_brain.nlu.communal.myInterface.StandardModule;
import com.robot_brain.nlu.flow.outsideApiCaller.OutsideApiCaller;
import com.robot_brain.nlu.flow.temp.SaveLikeRedis;

import java.util.Map;

/**
 * @ClassName BasisVerbs
 * @Description 基础动作类
 * @Author shark
 * @Date 2020/3/15 16:11
 * @Version V1.0
 **/
public class BasisVerbs extends ProcessLog {
    /*
     * create by: shark
     * description: TODO 应答
     * create time: 2020/3/15 16:35
     * @params [arrayParas, infoMap]
     * @return void
     */
    public void answer(String[] arrayParas, Map<String, String> infoMap) {
        //TODO @上文回复
        if (arrayParas.length == 0) {
            return;
        }
        String template = arrayParas[0];
        if (template.contains("<@")) {
            Extend extend = new Extend();
            template = extend.variablesReplace(template, infoMap);
        }
        infoMap.put("answer", template);
    }

    /*
     * create by: shark
     * description: TODO 信息获取，调用外部API
     * create time: 2020/3/15 16:53
     * @params [arrayParas, infoMap]
     * @return void
     */
    public void informationRetrieval(String[] arrayParas, Map<String, String> infoMap) {
        if (arrayParas.length == 0) {
            return;
        }
        String name = arrayParas[0];
        infoMap.put("信息获取",name);
//        TODO 调用外部API
        if (infoMap.containsKey("用户类型")){
            switch (infoMap.get("用户类型")){
                case "个人":
                    infoMap.put("用户类型","0");
                    break;
                case "单位":
                    infoMap.put("用户类型","1");
                    break;
            }

        }
        if (infoMap.containsKey("信息获取")){
            switch (infoMap.get("信息获取")){
                case "查询个人名下车牌号":
                    infoMap.put("funcation","1");
                    break;
                case "查询车牌对应的ETC卡号":
                    infoMap.put("funcation","2");
                    break;
                case "查询通行明细":
                    infoMap.put("funcation","3");
                    break;
            }

        }
        OutsideApiCaller outsideApiCaller =new OutsideApiCaller();
        //name = StandardModule._main_(infoMap).toString();
        name=outsideApiCaller._main_(infoMap);
        infoMap.put("answer", name);
    }

    /*
     * description: 新增，在infoMap中新增键值对
     * create by: shark
     * create time: 2020/3/30 10:35
     * @params [arrayParas, infoMap]
     * @return void
     */
    public void setInfo(String[] arrayParas, Map<String, String> infoMap) {
        if (arrayParas.length != 2) {
            return;
        }
        String key = arrayParas[0];
        String value = arrayParas[1];
        if (value.startsWith("@")) {
            // value属于一个变量,将它补全成<@变量名>的形式
            value = "<" + value + ">";
        }
        // 解析value中的"<@"变量
        if (value.contains("<@")) {
            Extend extend = new Extend();
            value = extend.variablesReplace(value, infoMap);
            this.addLog(extend.getLog());
        }
        // --
        infoMap.put(key, value);
        this.setLogInfo("【SET】" + key + "=" + value);
    }

    /*
     * description: 节点跳转
     * create by: shark
     * create time: 2020/3/30 10:39
     * @params [arrayParas, infoMap]
     * @return void
     */
    public void next(String[] arrayParas, Map<String, String> infoMap) {
        if (arrayParas.length == 0) {
            return;
        }
        String nodeID = arrayParas[0];
        if (nodeID.equals("break")) {
            infoMap.put("flowState", "break");
        } else {
            String flowID = infoMap.getOrDefault("流程ID", "");
            //需要对流程节点的有效性进行验证
            if (SaveLikeRedis.nodeRules.containsKey(flowID) && SaveLikeRedis.nodeRules.get(flowID).containsKey(nodeID)) {
                infoMap.put("当前节点名", nodeID);
                infoMap.put("flowState", "goto");
            } else {
                this.setLogInfo("节点跳转失败，节点\"" + nodeID + "\"不存在");
            }
        }
    }

    /*
     * description: 流程跳转
     * create by: shark
     * create time: 2020/3/30 10:40
     * @params [arrayParas, infoMap]
     * @return void
     */
    public void nextFlow(String[] arrayParas, Map<String, String> infoMap) {
        if (arrayParas.length == 0) {
            return;
        }
        //需要对流程的有效性进行验证
        String flowName = arrayParas[0];
        String flowID = SaveLikeRedis.flowName2ID.getOrDefault(flowName, "");
        if (flowID.length() > 0) {
            infoMap.put("流程ID", flowID);
            infoMap.put("流程名称", flowName);
            infoMap.put("flowState", "goto");
            infoMap.put("当前节点名","1");
        } else {
            this.setLogInfo("流程跳转失败，流程\"" + flowName + "\"不存在");
        }
    }

    /*
     * description: 提供选单
     * create by: shark
     * create time: 2020/3/30 10:45
     * @params [arrayParas, infoMap]
     * @return void
     */
    public void offerMenu(String[] arrayParas, Map<String, String> infoMap) {
        String pat = "";// 文字模板
        String chooseType = "";// 交互选择类型
        String chooseItem = "";// 选项内容
        switch (arrayParas.length) {
            case 3:
                pat = arrayParas[0];
                chooseType = arrayParas[1];
                chooseItem = arrayParas[2];
                break;
            case 2:
                // 交互选择类型默认为“上文无关”，补充选项默认为空
                pat = arrayParas[0];
                chooseType = "上文无关";
                chooseItem = arrayParas[1];
                break;
            default:
                this.setLogInfo("【菜单询问】缺少参数");
                return;
        }
        // 用于存储选项文本
        String[] chooseArray = {};
        Extend extend = new Extend();
        chooseItem = extend.variablesReplace(chooseItem, infoMap);

        if (chooseItem.contains("||")) {
            chooseArray = chooseItem.split("\\|\\|");
        } else {
//            TODO 从场景要素中获取选项
        }
        String chooseStr = "";
        for (int i = 0; i < chooseArray.length; i++) {
            int num=i+1;
            chooseStr += "[" + num + "]" + chooseArray[i] + " ";
        }
        //请选择您的车牌号：[1]苏L1234 [2]苏D12345
        infoMap.put("选项文本", chooseStr.trim());//[1]苏L1234 [2]苏D12345
        infoMap.put("answer", extend.variablesReplace(pat, infoMap));//请选择您的车牌号：[1]苏L1234 [2]苏D12345
        infoMap.put("选项内容",chooseItem);//苏L1234||苏D12345
        this.addLog(extend.getLog());
    }

    /*
     * description: 结束会话
     * create by: shark
     * create time: 2020/3/30 10:48
     * @params [arrayParas, infoMap]
     * @return void
     */
    public void endDialog(String[] arrayParas, Map<String, String> infoMap) {
        infoMap.put("流程结束标志", "true");
        infoMap.put("会话结束标志", "true");
        infoMap.put("flowState", "break");
    }

    /*
     * description: 结束流程
     * create by: shark
     * create time: 2020/3/30 10:56
     * @params [arrayParas, infoMap]
     * @return void
     */
    public void endFlow(String[] arrayParas, Map<String, String> infoMap) {
        infoMap.put("流程结束标志", "true");
        infoMap.put("flowState", "break");
    }
}
