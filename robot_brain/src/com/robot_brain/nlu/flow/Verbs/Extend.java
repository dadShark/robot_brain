package com.robot_brain.nlu.flow.Verbs;

import com.robot_brain.nlu.bean.ProcessLog;
import com.robot_brain.nlu.kit.StringTool;

import java.util.ArrayList;
import java.util.Map;

/**
 * @ClassName Extend
 * @Description 动作实现时需要进行复用的方法
 * @Author shark
 * @Date 2020/3/15 16:58
 * @Version V1.0
 **/
 class Extend extends ProcessLog {
     String variableSubstitution(String sentence, Map<String, String> infoMap) {
        //处理可选变量“[<@学校>]”
        boolean isReplaced = false;
        if (infoMap.size() == 0) {
            return sentence;
        }
        if (!sentence.contains("<@")) {
            return sentence;
        }
        ArrayList<String> optional = StringTool.getStringBetweenTag(sentence,
                "[", "]");
        if (optional.size() == 0) {
            return sentence;
        }
        String temp;
        String key;
        String value;
        for (String s : optional) {
            temp = s;
            key = "[" + s + "]";
            while (true) {
                int idx1 = temp.indexOf("<@");
                if (idx1 != -1) {
                    int idx2 = temp.indexOf(">", idx1);
                    String k = temp.substring(idx1 + 2, idx2);
                    String v = "";
                    if (infoMap.containsKey(k)) {
                        v = infoMap.get(k);
                    }
                    if (v.equals("<@")) {
                        // 防止出现值和变量一致的情况，导致死循环。
                        // 例子：待替换文本”在<@学校>的“，替换变量是”学校：<@学校>“
                        v = "";
                    }
                    if (!v.equals("")) {
                        temp = temp.replace("<@" + k + ">", v);
                        isReplaced = true;
                    } else {
                        temp = "";
                    }
                    // --
                } else
                    break;
            }
            value = temp;
            if (!s.equals(temp))
                sentence = sentence.replace(key, value);
        }
        if (!isReplaced) {// 为了标记所有的可选项都没有被替换
            if (!sentence.contains("<@") && !sentence.contains("允许无参数")) // 如果没有必选项，并且所有的可选项都为空，同时没有标志“[&允许无参数]”，则整个答案都为空。
            {
                super.setLog("【变量替换：】可选项全部为空");
                return "";
            }
        }
        //--
        //处理必选变量
        ArrayList<String> willChoose = StringTool.getStringBetweenTag(sentence,
                "<@", ">");
        if (optional.size() == 0) {
            return sentence;
        }
        for (String str : willChoose) {
            String vStr = "";
            if (infoMap.containsKey(str)) {
                vStr = infoMap.get(str);
            }
            if (vStr.equals("<@")) {
                // 防止出现值和变量一致的情况，导致死循环。
                // 例子：待替换文本”在<@学校>的“，替换变量是”学校：<@学校>“
                vStr = "";
            }
            if (vStr.length() == 0
                    && !sentence.contains("允许无参数")) {
                super.setLog(" 【规则变量替换：】必选项变量替换 \"<@" + str + ">\" 为空");
                return "";// 必选项为空 整个答案都为空
            }
            sentence = sentence.replace("<@" + str + ">", vStr);
        }
        //收尾处理
        sentence = sentence.replace("[&允许无参数]", "");
        sentence = sentence.replace("[&菜单选项]", "<@菜单选项>");
        return sentence;
    }
}
