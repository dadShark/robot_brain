package com.robot_brain.nlu.flow.temp;

import com.robot_brain.nlu.flow.FlowController;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * @ClassName FlowTry
 * @Description
 * @Author shark
 * @Date 2020/4/6 16:30
 * @Version V1.0
 **/
public class FlowTry {
    public static void main(String[] args){
        int i = 1;
        SaveLikeRedis saveLikeRedis=new SaveLikeRedis();
        Map<String, String> infoMap = new HashMap<>();
        infoMap.put("商家ID","10001");
        while (true) {
            System.out.println("第" + i + "轮对话");
            i++;
            System.out.println("请按照格式输入信息(key1=value1 key2=value2)，结束流程请输入end：");
            //输入信息：流程名称=入口：询问用户类型
            //输入信息2：用户类型=个人
            //开户姓名=张四 身份证号=341121199012120002
            //
            //车牌号=沪L12313
            //确认ETC卡号=是
            //查询日期=2020年4月6日
            Scanner reader = new Scanner(System.in);
            String input = reader.nextLine();
            if (input.equalsIgnoreCase("END")) {
                break;
            } else {
                try {
                    String[] arrayKeyValues = input.split(" ");
                    for (String keyValue : arrayKeyValues) {
                        String[] arrayStr = keyValue.split("=");
                        if (arrayStr.length == 2) {
                            infoMap.put(arrayStr[0], arrayStr[1]);
                        } else {
                            System.out.printf("ERROR：入参有误,等于符号使用不当。=>" + keyValue);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
                System.out.println("【进入流程】" + infoMap);
                FlowController flowController = new FlowController();
                flowController.runningFlow(infoMap);
                System.out.println("【结束流程】" + infoMap);
                System.out.println("【answer】" + infoMap.getOrDefault("answer", ""));
                System.out.println("【日志】");
                System.out.println(flowController.getLog());
            }
            infoMap.put("answer","");
        }
    }
}
