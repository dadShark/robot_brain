package com.robot_brain.nlu.flow;

import com.robot_brain.nlu.communal.kit.ProcessLog;
import com.robot_brain.nlu.communal.kit.StringTool;
import com.robot_brain.nlu.flow.Verbs.BasisVerbs;

import java.util.ArrayList;
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
        this.setLogDebug("开始执行规则："+rule);
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
            super.setLogInfo("规则格式错误:" + rule);
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
                //去除条件值中的双引号，不用做判断值类型（字符串或数值）的依据
                attr2 = attr2.replace("\"", "");
                //进行条件判断
                if (isTrue(attr1, operator, attr2, infoMap)) {
                    continue;
                }
                super.setLogDebug(condition + ",条件未达成");
            } else {
                super.setLogInfo("存在异常条件，无法解析：" + condition);
            }
            return false;
        }

        //动作执行
        super.setLogInfo("执行规则：" + rule);
        //解析动作
        ArrayList<String> actionsList = new ArrayList<>();
        if (actions.contains(");")) {
            // 用字符位置进行字符串切割
            int length = actions.length();
            int start = 0;
            while (start < length) {
                int end = actions.indexOf(");", start);
                if (end == -1) {
                    // 不存在");"则将剩下的整个字符串存入
                    end = length;
                } else {
                    // 后移一位，将")"算进去
                    end = end + 1;
                }
                actionsList.add(actions.substring(start, end));
                // 下次的开始是本次切割的末尾+1位置，用于去除";"
                start = end + 1;
            }
        } else {
            actionsList.add(actions.trim());
        }
        for (String verb : actionsList) {
            int front = verb.indexOf("(");
            int last = verb.lastIndexOf(")");
            //动作名称
            String verbName;
            //执行参数
            String[] arrayParas;
            if (front == -1) {
                //默认缺省的动作名称为"应答"
                verbName = "应答";
                arrayParas = new String[]{verb};
            } else {
                verbName = verb.substring(0, front).trim();
                arrayParas = verb.substring(front + 1, last).trim().split("\",\"");
            }
            int length = arrayParas.length;
            if (length > 0) {
                //去除前后遗留的引号
                arrayParas[0] = StringTool.trimCharacter(arrayParas[0], '\"');
                arrayParas[length - 1] = StringTool.trimCharacter(arrayParas[length - 1], '\"');
            }
            String answer = infoMap.getOrDefault("answer", "");
            doVerb(verbName, arrayParas, infoMap);
            if (answer.length() > 0 && !answer.equals(infoMap.getOrDefault("answer", ""))) {
                //当同一条规则的多组动作对答案进行操作时，默认直接进行答案拼接
                infoMap.put("answer", answer + infoMap.getOrDefault("answer", ""));
            }
        }
        return true;
    }

    /*
     * create by: shark
     * description: 判断条件是否成立
     * create time: 2020/3/23 9:16
     * @params [attr1, operator, attr2, infoMap]
     * @return java.lang.Boolean
     */
    private Boolean isTrue(String attr1, String operator, String attr2, Map<String, String> infoMap) {
        String[] arrayAttr2;
        if (attr2.contains("||")) {
            //多个值视为or的关系，一个为true，整个表达式为true
            arrayAttr2 = attr2.split("\\|\\|");
        } else {
            arrayAttr2 = new String[]{attr2};
        }
        for (String strSetAttr2 : arrayAttr2) {
            //当前值是否为数值，默认当作字符串类型解析
            boolean isNum = false;
            //当前值（数值类型），isNum为true时启用。
            double dbCurAttr2 = -1;
            //当前值（字符串类型）
            String strCurAttr2 = "";
            //设定值(数值类型)
            double dbSetAttr2 = -1;
            if (attr1.contains("%") || attr1.contains("/")) {
                //attr1可能是除法算式,需要先计算其结果，再进行比较。如：交互回合数%3=0
                int tag1 = attr1.indexOf("%");
                int tag2 = attr1.indexOf("/");
                //除数
                String tmp = "";
                //被除数
                String value = "";
                //运算符
                String op = "";
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
                    tmp = infoMap.getOrDefault(tmp, "");
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
                    //当前值获取完毕，默认是String
                    strCurAttr2 = infoMap.getOrDefault(attr1, "");
                }
                if (strSetAttr2.startsWith("@") && infoMap.containsKey(strSetAttr2)) {
                    //出发地=@目的地
                    strSetAttr2 = infoMap.getOrDefault(strSetAttr2, "");
                }
                if (StringTool.isArabicNumerals(strCurAttr2) && StringTool.isArabicNumerals(strSetAttr2)) {
                    //如果设定值和当前值都是数值，则此次是数值运算
                    isNum = true;
                    dbCurAttr2 = Double.parseDouble(strCurAttr2);
                    dbSetAttr2 = Double.parseDouble(attr2);
                }
            }
            if (isNum) {
                switch (operator) {
                    case "=":
                        if (dbCurAttr2 == dbSetAttr2) {
                            return true;
                        }
                        break;
                    case "!=":
                        if (dbCurAttr2 != dbSetAttr2) {
                            return true;
                        }
                        break;
                    case "<=":
                        if (dbCurAttr2 <= dbSetAttr2) {
                            return true;
                        }
                        break;
                    case "<":
                        if (dbCurAttr2 < dbSetAttr2) {
                            return true;
                        }
                        break;
                    case ">=":
                        if (dbCurAttr2 >= dbSetAttr2) {
                            return true;
                        }
                        break;
                    case ">":
                        if (dbCurAttr2 > dbSetAttr2) {
                            return true;
                        }
                        break;
                }
            } else {
                switch (operator) {
                    case "=":
                        if (strSetAttr2.equals(strCurAttr2) || (strSetAttr2.equals("存在") && strCurAttr2.length() > 0)) {
                            return true;
                        }
                        break;
                    case "!=":
                        if (!strCurAttr2.equals(strSetAttr2)) {
                            return true;
                        }
                        break;
                    case "contains":
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
    private void doVerb(String verbName, String[] arrayParas, Map<String, String> infoMap) {
        if (arrayParas == null) {
            arrayParas = new String[]{};
        }
        BasisVerbs basisVerbs = new BasisVerbs();
        switch (verbName) {
            case "应答":
                basisVerbs.answer(arrayParas, infoMap);
                break;
            case "信息获取":
                basisVerbs.informationRetrieval(arrayParas, infoMap);
                break;
            case "新增":
                basisVerbs.setInfo(arrayParas,infoMap);
                break;
            case "节点跳转":
                basisVerbs.next(arrayParas,infoMap);
                break;
            case "流程跳转":
                basisVerbs.nextFlow(arrayParas, infoMap);
                break;
            case "提供选单":
                basisVerbs.offerMenu(arrayParas, infoMap);
                break;
            case "结束会话":
                basisVerbs.endDialog(arrayParas, infoMap);
                break;
            case "结束流程":
                basisVerbs.endFlow(arrayParas, infoMap);
                break;
        }
        super.addLog(basisVerbs.getLog());
    }
}
