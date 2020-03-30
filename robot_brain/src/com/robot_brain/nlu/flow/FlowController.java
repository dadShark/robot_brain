package com.robot_brain.nlu.flow;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.robot_brain.nlu.communal.kit.ProcessLog;
import com.robot_brain.nlu.flow.bean.FlowRunningHistory;
import com.robot_brain.nlu.flow.temp.SaveLikeRedis;
import org.jetbrains.annotations.NotNull;

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
            //TODO:根据中心问题检索需要进入的流程
            if (flowID.equals("NULL")) {
                //TODO:根据流程执行历史信息，进入流程
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
        boolean flowBreak = false;
        FlowRuleRunner runner = new FlowRuleRunner();
        //初始化流程信息
        infoMap.put("当前节点名", nodeNum);
        infoMap.put("流程名称", flowName);
        infoMap.put("流程ID", flowID);
        while (!flowBreak) {
            //记录是否有规则已执行
            boolean isRun = false;
            //加载节点规则
            for (String rule : loadNodeRules(flowID, nodeNum)) {
                if (runner.runRule(rule, infoMap)) {
                    isRun = true;
                    infoMap.put("结束规则",rule);
                    String _nodeNum = infoMap.getOrDefault("当前节点名", "");
                    String _flowID = infoMap.getOrDefault("流程ID", "");
                    if (nodeNum.equals("break")) {
                        flowBreak = true;
                        break;
                    }
                    if (!_nodeNum.equals(nodeNum) || _flowID.equals(flowID)) {
                        //节点号或者流程ID发生改变，终止本节点的规则执行，进入下一个节点或流程
                        nodeNum = _nodeNum;
                        flowID = _flowID;
                        break;
                    }
                }
            }
            if (!isRun) {
                this.setLogInfo("无满足条件的规则可以执行，终止流程");
                //跳出，防止死循环。
                break;
            }
        }
        //获取执行日志
        this.setLogInfo(runner.getLog());
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
