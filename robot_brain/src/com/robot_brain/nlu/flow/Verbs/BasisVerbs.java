package com.robot_brain.nlu.flow.Verbs;

import com.robot_brain.nlu.communal.kit.ProcessLog;

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
    public  void answer(String[] arrayParas, Map<String,String> infoMap) {
        //TODO @上文回复
        if (arrayParas.length == 0) {
            return;
        }
        String template = arrayParas[0];
        if (template.contains("<@")) {
            Extend extend=new Extend();
            template=extend.variablesReplace(template,infoMap);
        }
        infoMap.put("answer", template);
    }

    /*
     * create by: shark
     * description: TODO 调用外部API
     * create time: 2020/3/15 16:53
     * @params [arrayParas, infoMap]
     * @return void
     */
    public  void informationRetrieval(String[] arrayParas, Map<String,String> infoMap) {
        if(arrayParas.length==0){
            return;
        }
        String name=arrayParas[0];
//        TODO 调用外部API
        infoMap.put("结果",name);
    }
}
