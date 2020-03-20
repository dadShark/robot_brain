package com.robot_brain.nlu.flow.kit;

import java.util.Properties;

public class GenericUntil {
    /**
     * 描述：@description 读取全局配置文件参数
     * 参数：@param key 参数：@return value
     * 返回值类型：@returnType String
     * 创建时间：@dateTime 2020年3月20日 15:35:59
     * 作者：@author waterkingko
     */
    public static String getGlobalProfileInfo(String key) {

        return GenericUntil.getXMLInfo(key, "commonLibGlobal");
    }

    /**
     *
     *描述：从xml文件中读取配置
     *
     * @author: qianlei
     *@date： 日期：2019-9-4 时间：上午09:45:10
     *@param node
     *@param fileName
     *@return
     *@throws Exception
     */
    public static String getXMLInfo(String node, String fileName) {
        String path = getAppPath(GenericUntil.class, fileName);
        Properties props = new Properties();

        try {
            InputStream xmlInputStream = new FileInputStream(path);
            if (xmlInputStream == null) {
                return "";
            }
            props.loadFromXML(xmlInputStream);
            return props.getProperty(node);
        } catch (Exception e) {
            GlobalValue.myLog.error("【" + fileName + "配置缺失】" + node, e);
            return "";
        }
    }
}
