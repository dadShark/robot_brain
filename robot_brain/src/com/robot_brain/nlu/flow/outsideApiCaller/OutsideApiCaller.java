package com.robot_brain.nlu.flow.outsideApiCaller;

import com.robot_brain.nlu.communal.myInterface.StandardModule;


public class OutsideApiCaller implements StandardModule {
    public void _init_() {
        //判断redis中key对应value是否为空，非空则清空redis对应value

        //从数据库中查表获取第三方接口配置名，存入javabean结构中
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
