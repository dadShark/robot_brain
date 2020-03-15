package com.robot_brain.nlu.flow;

import com.robot_brain.nlu.bean.ProcessLog;
import com.robot_brain.nlu.flow.Verbs.BasisVerbs;
import com.robot_brain.nlu.kit.StringTool;

import java.util.Map;

/**
 * @ClassName FlowRuleRunner
 * @Description 运行已经标准化后的规则
 * 通信网络="本网" and 第一次来电="是" and 计费状态="001" and 余额判断>600 ==> 应答("您的话费余额是700分");信息获取("下发短信");应答("<@结果>")
 * @Author shark
 * @Date 2020/3/15 10:34
 * @Version V1.0
 **/
public class FlowRuleRunner extends ProcessLog {

    public Boolean runRule(String rule, Map<String, String> infoMap) {
        //规则解析
        String conditions = "";
        String actions;
        String[] arrayRule = rule.split("==>");
        if (arrayRule.length == 2) {
            actions = arrayRule[1];
            conditions = arrayRule[0];
        } else if (arrayRule.length == 1) {
            actions = arrayRule[0];
        } else {
            super.setLog("规则格式错误:" + rule);
            return false;
        }
        //条件判断
        String[] arrayConditions = conditions.split("and");
        for (String condition : arrayConditions) {
            //特殊条件：没有运算符
            if (condition.toLowerCase().trim().equals("true")) {
                //条件为true,证明无条件执行
                break;
            }
            //--
            //解析表达式中的运算符
            int noEqual = condition.indexOf("!=");
            int equal = condition.indexOf("=");
            int contains = condition.indexOf("contains");
            int moreOrEqual = condition.indexOf(">=");
            int lessOrEqual = condition.indexOf("<=");
            int more = condition.indexOf(">");
            int less = condition.indexOf("<");
            int tag = -1;
            String operator = "";
            if (noEqual != -1) {
                operator = "!=";
                tag = noEqual;
            } else if (contains != -1) {
                operator = "contains";
                tag = contains;
            } else if (moreOrEqual != -1) {
                operator = ">=";
                tag = moreOrEqual;
            } else if (lessOrEqual != -1) {
                operator = "<=";
                tag = lessOrEqual;
            } else if (more != -1) {
                operator = ">";
                tag = more;
            } else if (less != -1) {
                operator = "<";
                tag = more;
            } else if (equal != -1) {
                operator = "=";
                tag = equal;
            }

            if (tag != -1) {
                String attr1 = condition.substring(0, tag).trim();
                String attr2 = condition.substring(tag + operator.length()).trim();
                //进行条件判断
                if (isTrue(attr1, operator, attr2, infoMap)) {
                    continue;
                }
                super.setLog(condition + ",条件未达成");
                return false;
            } else {
                super.setLog("存在异常条件，无法解析：" + condition);
                return false;
            }
        }
        //动作执行
        super.setLog("执行规则："+rule);
        //解析动作
        String[] arrayActions=actions.split("\\);");
        for(String verb:arrayActions){
            int tag=verb.indexOf("(");
            String verbName;//动作名称
            String[] arrayParas;//执行参数
            if(tag==-1){
                //默认缺省的动作名称为"应答"
                verbName="应答";
                arrayParas=new String[]{verb};
            }else{
                verbName=verb.substring(0,tag);
                arrayParas=verb.substring(tag+1).split("\",");
            }
            doVerb(verbName,arrayParas,infoMap);
        }
        return true;
    }

    private Boolean isTrue(String attr1, String operator, String attr2, Map<String, String> infoMap) {
        String[] arrayAttr2;
        if (attr2.contains("||")) {
            arrayAttr2 = attr2.split("\\|\\|");//多个值视为or的关系，一个为true，整个表达式为true
        } else {
            arrayAttr2 = new String[]{attr2};
        }
        for (String strSetAttr2 : arrayAttr2) {
            boolean isNum = false;//当前值是否为数值，默认当作字符串类型解析
            double dbCurAttr2 = -1;//当前值（数值类型），isNum为true时启用。
            String strCurAttr2 = "";//当前值（字符串类型）
            double dbSetAttr2 = -1;//设定值(数值类型)
            if (attr1.contains("%") || attr1.contains("/")) {
                //attr1可能是除法算式,需要先计算其结果，再进行比较。如：交互回合数%3=0
                int tag1 = attr1.indexOf("%");
                int tag2 = attr1.indexOf("/");
                String tmp = "";//除数
                String value = "";//被除数
                String op = "";//运算符
                if (tag1 != -1) {
                    tmp = attr1.substring(0, tag1);
                    value = attr1.substring(tag1 + 1);
                    op = "%";
                } else if (tag2 != -1) {
                    tmp = attr1.substring(0, tag2);
                    value = attr1.substring(tag2 + 1);
                    op = "/";
                }
                if (infoMap.containsKey(tmp)) {
                    tmp = infoMap.get(tmp);
                }
                if (StringTool.isArabicNumerals(tmp) && StringTool.isArabicNumerals(value) && StringTool.isArabicNumerals(attr2)) {
                    if (op.equals("%")) {
                        dbCurAttr2 = Double.parseDouble(tmp) % Double.parseDouble(value);
                        dbSetAttr2 = Double.parseDouble(strSetAttr2);
                        isNum = true;
                    } else if (op.equals("/")) {
                        dbCurAttr2 = Double.parseDouble(tmp) / Double.parseDouble(value);
                        dbSetAttr2 = Double.parseDouble(strSetAttr2);
                        isNum = true;
                    }
                }
                //不论算式解析是否成功，都需要继续往下执行
            }
            if (!isNum) {
                //attr1非算式的情况，需要从infoMap中取值，并判断此次是数值运算还是字符运算
                if (infoMap.containsKey(attr1)) {
                    strCurAttr2 = infoMap.get(attr1);//当前值获取完毕，默认是String
                }
                if (strSetAttr2.startsWith("@") && infoMap.containsKey(strSetAttr2)) {
                    //出发地=@目的地
                    strSetAttr2 = infoMap.get(strSetAttr2);
                }
                if (StringTool.isArabicNumerals(strCurAttr2) && StringTool.isArabicNumerals(strSetAttr2)) {
                    //如果设定值和当前值都是数值，则此次是数值运算
                    isNum = true;
                    dbCurAttr2 = Double.parseDouble(strCurAttr2);
                    dbSetAttr2 = Double.parseDouble(attr2);
                }
            }
            if (isNum) {
                if (operator.equals("=")) {
                    if (dbCurAttr2 == dbSetAttr2) {
                        return true;
                    }
                } else if (operator.equals("!=")) {
                    if (dbCurAttr2 != dbSetAttr2) {
                        return true;
                    }
                } else if (operator.equals("<=")) {
                    if (dbCurAttr2 <= dbSetAttr2) {
                        return true;
                    }
                } else if (operator.equals("<")) {
                    if (dbCurAttr2 < dbSetAttr2) {
                        return true;
                    }
                } else if (operator.equals(">=")) {
                    if (dbCurAttr2 >= dbSetAttr2) {
                        return true;
                    }
                } else if (operator.equals(">")) {
                    if (dbCurAttr2 > dbSetAttr2) {
                        return true;
                    }
                }
            } else {
                if (operator.equals("=")) {
                    if (strSetAttr2.equals(strCurAttr2) || (strSetAttr2.equals("存在") && strCurAttr2.length() > 0)) {
                        return true;
                    }
                } else if (operator.equals("!=")) {
                    if (!strCurAttr2.equals(strSetAttr2)) {
                        return true;
                    }
                } else if (operator.equals("contains")) {
                    return strCurAttr2.contains(strSetAttr2);
                }
            }


        }
        return false;
    }

    /*
     * create by: shark
     * description: 按照动作名称，执行动作
     * create time: 2020/3/15 16:34
     * @params [verbName, arrayParas, infoMap]
     * @return void
     */
    public  void doVerb(String verbName, String[] arrayParas, Map<String, String> infoMap) {
        if (verbName.equals("应答")) {
            BasisVerbs.answer(arrayParas, infoMap);
        }else if(verbName.equals("信息获取")){
            BasisVerbs.informationRetrieval(arrayParas,infoMap);
        }
    }
}
