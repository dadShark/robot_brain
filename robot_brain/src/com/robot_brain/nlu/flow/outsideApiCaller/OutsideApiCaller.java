package com.robot_brain.nlu.flow.outsideApiCaller;

import com.robot_brain.nlu.communal.myInterface.StandardModule;
import com.robot_brain.nlu.flow.bean.OutsideApiInfo;
import com.robot_brain.nlu.flow.kit.OutsideApiDAO;
import com.robot_brain.nlu.flow.kit.RedisUntil;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;

import javax.servlet.jsp.jstl.sql.Result;
import java.util.ArrayList;
import java.util.SortedMap;


public class OutsideApiCaller implements StandardModule {
    public void _init_() {
        //思路:查出答案之后直接将答案往redis中存。每次存之前清空对应KEY下的内容。
        Result dt;
        try {
            dt = OutsideApiDAO.select("flow_interface_t");//第三方接口信息配制
            if (dt == null) {
                LogUntil.error("LoadTheThirdInterfaceInfo(第三方接口信息配制)中获取接口参数输出配制表信息为空！！！");//日志输出函数·暂定
                return;
            }

        //判断redis中key对应value是否为空，非空则清空redis对应value
        Jedis checkvalue = new Jedis();

        //从数据库中查表获取第三方接口配置名，存入javabean结构中
        //
        //        字段	描述
        //        id	主键
        //        interface_name	接口名称
        //        interface_url	接口地址
        //        invocation_way	调用方式（HTTP，WEBSERVICE）
        //        function_name	请求方式（调用方式为http时，为GET、POST；调用方式为webservice时，
        //        namespace	命名空间 调用方式为webservice时填写
        //        req_param	请求参数 多个参数名称以,连接
        //        resp_param	响应参数 多个参数名称以,连接
        //        application_id	应用ID
        //sql: select application_id,invocation_way, function_name, namespace, req_param,resp_param from flow_interface_t where interface_name =?;
        //将结构体转换为json格式存入redis的key下
    }
    /*
    *
    *
    *
     */
    public String _main_(String conversationID) {
        //根据第三方配置名从redis中将json串取出到map中
        //判断map中元素invocation_way判断使用哪种解析接口方式HTTP，WEBSERVICE
        return null;
    }
}
