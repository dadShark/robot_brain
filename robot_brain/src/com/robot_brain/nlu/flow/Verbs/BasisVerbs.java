package com.robot_brain.nlu.flow.Verbs;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * @ClassName BasisVerbs
 * @Description 基础动作类
 * @Author shark
 * @Date 2020/3/15 16:11
 * @Version V1.0
 **/
public class BasisVerbs {
    /*
     * create by: shark
     * description: TODO 应答
     * create time: 2020/3/15 16:35
     * @params [arrayParas, infoMap]
     * @return void
     */
    public static void answer(String[] arrayParas, Map<String, String> infoMap) {
        //TODO @上文回复
        if (arrayParas.length == 0) {
            return;
        }
        String template = arrayParas[0];
        if (template.contains("<@")) {
            Extend extend=new Extend();
            template=extend.variablesReplace(template,infoMap);
        }
        if (infoMap.containsKey("answer")) {
            infoMap.put("answer", infoMap.get("answer") + template);
        } else {
            infoMap.put("answer", template);
        }
    }

    /*
     * create by: shark
     * description: TODO 调用外部API
     * create time: 2020/3/15 16:53
     * @params [arrayParas, infoMap]
     * @return void
     */
    public static void informationRetrieval(@NotNull String[] arrayParas, Map<String, String> infoMap) {
        if(arrayParas.length==0){
            return;
        }
        String name=arrayParas[0];
//        TODO 调用外部API
        infoMap.put("结果",name);
    }
}
