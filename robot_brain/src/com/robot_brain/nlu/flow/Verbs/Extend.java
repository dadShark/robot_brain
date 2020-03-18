package com.robot_brain.nlu.flow.Verbs;

import com.robot_brain.nlu.communal.kit.ProcessLog;
import com.robot_brain.nlu.communal.kit.StringTool;

import java.util.ArrayList;
import java.util.Map;

/**
 * @ClassName Extend
 * @Description 动作实现时需要进行复用的方法
 * @Author shark
 * @Date 2020/3/15 16:58
 * @Version V1.0
 **/
public class Extend extends ProcessLog {
    public  String variablesReplace(String sentence, Map<String, String> infoMap) {
        //处理可选变量“[漫游国是<@国家>]”
        if (infoMap.size() == 0) {
            return sentence;
        }
        if (!sentence.contains("<@")) {
            return sentence;
        }
        ArrayList<String> optional = StringTool.getStringBetweenTag(sentence,
                "[", "]");
        if (optional.size() != 0) {
            boolean isReplaced = false;
            for (String s : optional) {
                String temp = s;
                String key = "[" + s + "]";
                //可选模块内部，执行必选变量替换流程
                temp=requiredVariablesReplace(temp, infoMap);
                if (!key.equals(temp)) {
                    isReplaced = true;
                    sentence = sentence.replace(key, temp);
                }
            }

            if (!isReplaced && !sentence.contains("<@") && !sentence.contains("允许无参数")) // 如果没有必选项，并且所有的可选项都为空，同时没有标志“[&允许无参数]”，则整个答案都为空。
            {
                super.setLogInfo("因为可选项全部为空,所以将文本清空。");
                return "";
            }
        }
        //--
        sentence=requiredVariablesReplace(sentence, infoMap);
        //处理必选变量

        //收尾处理
        sentence = sentence.replace("[&允许无参数]", "");
        sentence = sentence.replace("[&菜单选项]", "<@菜单选项>");
        return sentence;
    }

    /*
     * create by: shark
     * description: 必选变量替换
     * create time: 2020/3/17 8:16
     * @params [sentence, infoMap]
     * @return java.lang.Boolean
     */
    private String requiredVariablesReplace(String sentence, Map<String, String> infoMap) {
        ArrayList<String> willChoose = StringTool.getStringBetweenTag(sentence,
                "<@", ">");
        if (willChoose.size() == 0) {
            return sentence;//没有找到可替换变量，返回原串
        }
        for (String str : willChoose) {
            String vStr = "";
            if (infoMap.containsKey(str)) {
                vStr = infoMap.get(str);
            }
            if (vStr.length() == 0
                    && !sentence.contains("允许无参数")) {
                super.setLogInfo(" 【规则变量替换：】必选项变量替换 \"<@" + str + ">\" 为空");
                return "";// 必选项为空 整个答案都为空
            }
            sentence = sentence.replace("<@" + str + ">", vStr);
        }
        return sentence;
    }
}
