package com.robot_brain.nlu.kit;

import java.util.ArrayList;

/**
 * @ClassName StringTool
 * @Description 字符串处理的工具类
 * @Author shark
 * @Date 2020/3/15 13:32
 * @Version V1.0
 **/
public class StringTool {
    /*
     * create by: shark
     * description: 判断是否是阿拉伯数字
     * create time: 2020/3/15 13:33
     * @params [str]
     * @return java.lang.Boolean
     */
    public static Boolean isArabicNumerals(String str) {
        int tag1 = str.indexOf(".");
        int tag2 = str.indexOf(".", tag1 + 1);
        if (tag2 != -1) {
            // 字符串中有多个小数点，不是数字串
            return false;
        }
        str = str.replace(".", "");// 去除小数点

        if (str.length() == 0)
            return false;

        for (char c : str.toCharArray()) {
            if (c == '0' || c == '1' || c == '2' || c == '3' || c == '4'
                    || c == '5' || c == '6' || c == '7' || c == '8' || c == '9') {
                continue;
            }
            return false;
        }
        return true;
    }

    /*
     * create by: shark
     * description: 截取两个字符之间的内容
     * create time: 2020/3/15 17:06
     * @params [str, beg, end]
     * @return java.util.ArrayList<java.lang.String>
     */
    public static ArrayList<String> getStringBetweenTag(String str,
                                                        String beg, String end) {
        ArrayList<String> resultList = new ArrayList<String>();
        while (str.length() > 0) {
            int tag = str.indexOf(beg);
            if (tag == -1) {
                break;
            }
            int endTag = str.indexOf(end, tag + 1);
            if (endTag == -1) {
                break;
            }
            if ((endTag - tag - beg.length()) > 0 && tag + beg.length() < str.length()) {
                String result = str.substring(tag + beg.length(), tag
                        + beg.length() + endTag - tag - beg.length());
                resultList.add(result);
                str = str.substring(endTag + 1, endTag + 1
                        + str.length() - endTag - 1);
            } else {
                break;
            }
        }
        return resultList;
    }
}
