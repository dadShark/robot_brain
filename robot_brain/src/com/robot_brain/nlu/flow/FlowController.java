package com.robot_brain.nlu.flow;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.robot_brain.nlu.communal.kit.ProcessLog;
import com.robot_brain.nlu.flow.bean.FlowRunningHistory;
import com.robot_brain.nlu.flow.temp.SaveLikeRedis;
import com.sun.istack.internal.NotNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

/**
 * @ClassName FlowController
 * @Description 流程控制器（main）
 * @Author shark
 * @Date 2020/3/29 13:30
 * @Version V1.0
 **/
public class FlowController extends ProcessLog {

    public void runningFlow(@NotNull Map<String, String> infoMap) {
        //--判断本次需要进入的流程
        //根据流程名称进入流程
        String flowName = infoMap.getOrDefault("流程名称", "");
        String flowID = loadFlowIDbyFlowName(flowName);
        if (flowID.equals("NULL")) {
            this.setLogInfo("场景：" + flowName + "不存在");
            infoMap.put("无效流程名称", flowName);
            infoMap.remove("流程名称");
            //TODO:根据流程执行历史信息，进入流程
            if (flowID.equals("NULL")) {
                //TODO:根据中心问题检索需要进入的流程
            }
        }
        if (flowID.equals("NULL")) {
            this.setLogInfo("无可执行的流程");
            return;
        }
        //--
        //查询流程执行历史记录，确定当前所处的流程节点。
        String dialogID = infoMap.getOrDefault("会话ID", "");
        FlowRunningHistory history = loadFlowHistory(dialogID);
        String nodeNum = "1";
        if (!history.getEndNode()) {
            //历史流程没有结束，本次继续执行
            nodeNum = history.getNodeNum();
        }
        //开始执行流程
        boolean go = true;
        //初始化流程信息
        infoMap.put("当前节点名", nodeNum);
        infoMap.put("流程名称", flowName);
        infoMap.put("流程ID", flowID);
        //流程状态，取值有两种：break（跳出流程）,goto（进行节点跳转或流程跳转）
        String flowState = "";
        while (go) {
            FlowRuleRunner runner = new FlowRuleRunner();
            //加载并执行节点规则
            for (String rule : loadNodeRules(flowID, nodeNum)) {
                //当节点规则遍历完成之后结束循环，或者当规则要求break时结束循环。
                if (runner.runRule(rule, infoMap)) {
                    infoMap.put("结束规则", rule);
                    flowState = infoMap.getOrDefault("flowState", "");
                    if (flowState.equals("break") || flowState.equals("goto")) {
                        break;
                    }
                }
            }
            //获取执行日志
            this.addLog(runner.getLog());
            switch (flowState) {
                case "break":
                    //流程终止信号
                    go = false;
                    break;
                case "goto":
                    //流程跳转信号
                    go = true;
                    flowID = infoMap.getOrDefault("流程ID", "");
                    nodeNum = infoMap.getOrDefault("当前节点名", "");
                    break;
                default:
                    //无信号时，为了防止死循环，默认终止流程
                    go = false;
                    this.setLogInfo("规则存在异常，无终止信号和跳转信号，系统强制终止");
                    break;
            }
            //去除状态值，以免影响下次执行
            infoMap.remove("flowState");
            flowState="";
        }
        //将流程执行的情况，记录进流程历史记录中
        FlowRunningHistory flowRunningHistory = new FlowRunningHistory(infoMap);
        saveFlowHistory(dialogID, flowRunningHistory);
    }

    private ArrayList<String> loadNodeRules(String flowID, String nodeID) {
        ArrayList<String> list = new ArrayList<>();
        if (SaveLikeRedis.nodeRules.containsKey(flowID) && SaveLikeRedis.nodeRules.get(flowID).containsKey(nodeID)) {
            String jsonRules = SaveLikeRedis.nodeRules.get(flowID).get(nodeID);
            JSONArray jsonArray = JSON.parseArray(jsonRules);
            list.addAll(jsonArray.toJavaList(String.class));
        }
        return list;
    }

    private FlowRunningHistory loadFlowHistory(String dialogID) {
        if (SaveLikeRedis.flowRunningHistory.containsKey(dialogID)) {
            return SaveLikeRedis.flowRunningHistory.get(dialogID).getLast();
        } else {
            //没有历史记录，默认传出一个空的结束节点
            return new FlowRunningHistory();
        }
    }

    private void saveFlowHistory(String dialogID, FlowRunningHistory history) {
        if (SaveLikeRedis.flowRunningHistory.containsKey(dialogID)) {
            SaveLikeRedis.flowRunningHistory.get(dialogID).add(history);
        } else {
            LinkedList<FlowRunningHistory> list = new LinkedList<>();
            list.add(history);
            SaveLikeRedis.flowRunningHistory.put(dialogID, list);
        }
    }

    private String loadFlowIDbyFlowName(String flowName) {
        return SaveLikeRedis.flowName2ID.getOrDefault(flowName, "NULL");
    }

}
