package com.robot_brain.nlu.flow.bean;

import com.sun.istack.internal.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName FlowRunningHistory
 * @Description 流程执行历史记录，从redis中读取数据后，将信息保存到FlowRunningHistory对象中。
 * @Author shark
 * @Date 2020/3/29 13:30
 * @Version V1.0
 **/
public class FlowRunningHistory {
    //流程名称
    private String flowName = "";
    //流程ID
    private String flowID = "";
    //节点ID（节点没有名称）
    private String nodeNum = "";
    //结束规则：本次执行最终停止的规则，即执行的最后一条规则
    private String consequentialRule = "";
    //结束规则的ID
    private String consequentialRuleID = "";
    //流程的结束标志：为true证明流程已经结束，反之为false
    private Boolean isEndNode = true;
    //结束状态：本次执行停止之后的infoMap
    private Map<String, String> infoMap = new HashMap<>();

    public FlowRunningHistory() {

    }

    public FlowRunningHistory(@NotNull Map<String, String> _infoMap) {
        for (String key : _infoMap.keySet()) {
            infoMap.put(key, _infoMap.get(key));
        }
        flowName = infoMap.getOrDefault("流程名称", "");
        flowID = infoMap.getOrDefault("流程ID", "");
        nodeNum = infoMap.getOrDefault("当前节点名", "");
        consequentialRule = infoMap.getOrDefault("结束规则", "");
        consequentialRuleID = infoMap.getOrDefault("结束规则ID", "");
        try {
            isEndNode = Boolean.parseBoolean(infoMap.getOrDefault("流程结束标志", "false"));
        } catch (Exception e) {
            isEndNode = true;
            //TODO 记录错误日志
        }
    }

    public String getFlowName() {
        return flowName;
    }

    public String getFlowID() {
        return flowID;
    }

    public String getNodeNum() {
        return nodeNum;
    }

    public String getConsequentialRule() {
        return consequentialRule;
    }

    public String getConsequentialRuleID() {
        return consequentialRuleID;
    }

    public Boolean getEndNode() {
        return isEndNode;
    }

    public Map<String, String> getInfoMap() {
        return infoMap;
    }

    public void setFlowName(String flowName) {
        this.flowName = flowName;
    }

    public void setFlowID(String flowID) {
        this.flowID = flowID;
    }

    public void setNodeNum(String nodeNum) {
        this.nodeNum = nodeNum;
    }

    public void setConsequentialRule(String consequentialRule) {
        this.consequentialRule = consequentialRule;
    }

    public void setConsequentialRuleID(String consequentialRuleID) {
        this.consequentialRuleID = consequentialRuleID;
    }

    public void setEndNode(Boolean endNode) {
        isEndNode = endNode;
    }

    public void setInfoMap(Map<String, String> infoMap) {
        this.infoMap = infoMap;
    }
}
