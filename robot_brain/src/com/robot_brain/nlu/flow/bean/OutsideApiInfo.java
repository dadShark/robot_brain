package com.robot_brain.nlu.flow.bean;

import com.sun.xml.internal.bind.v2.runtime.reflect.Lister;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class OutsideApiInfo {


    /**
     * @ClassName OutsideApiInfo
     * @Description 第三方接口表中的配置信息
     * @Author waterkingko
     * @Date 2020年3月19日 00:35:42
     * @Version V1.0
     * @deprecated 暂时不用该方法
     **/
    private String InterfaceName;//接口名称
    private String Business;//商家
    private String InterfaceAddress;//接口地址
    private String CallingMethod;//调用方式（HTTP，WEBSERVICE）
    private String RequestMethod;//（调用方式为http时，为GET、POST；调用方式为webservice时，为函数名称）
    private String NameSpace;//命名空间 调用方式为webservice时填写
    private List ParseRequestValue;//请求参数 多个参数名称以,连接
    private List ResponseParameter;//响应参数 多个参数名称以,连接
    private String ApplicationID;//应用ID

    public OutsideApiInfo() {
        InterfaceName = "";
        Business = "";
        ParseRequestValue = new LinkedList();
        ResponseParameter = new LinkedList();
        RequestMethod = "";
    }

    public OutsideApiInfo(OutsideApiInfo apiInfo) {
        if (apiInfo == null) {
            return;
        }
        try {
            this.InterfaceName = apiInfo.InterfaceName;
            this.InterfaceAddress = apiInfo.InterfaceAddress;
            this.Business = apiInfo.Business;
            this.RequestMethod = apiInfo.RequestMethod;
            this.CallingMethod = apiInfo.CallingMethod;
            this.NameSpace = apiInfo.NameSpace;
            this.ParseRequestValue = apiInfo.ParseRequestValue;
            this.ResponseParameter = apiInfo.ResponseParameter;
            this.ApplicationID = apiInfo.ApplicationID;

        } catch (Exception e) {
            System.out.println("获取第三方接口配置出错");
        }
    }

    public String getInterfaceName() {
        return InterfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.InterfaceName = interfaceName;
    }

    public String getBusiness() {
        return Business;
    }

    public void setBusiness(String business) {
        this.Business = business;
    }

    public String getInterfaceAddress() {
        return InterfaceAddress;
    }

    public void setInterfaceAddress(String interfaceAddress) {
        InterfaceAddress = interfaceAddress;
    }

    public String getCallingMethod() {
        return CallingMethod;
    }

    public void setCallingMethod(String callingMethod) {
        this.CallingMethod = callingMethod;
    }

    public String getRequestMethod() {
        return RequestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.RequestMethod = requestMethod;
    }

    public String getNameSpace() {
        return NameSpace;
    }

    public void setNameSpace(String nameSpace) {
        this.NameSpace = nameSpace;
    }

    public List getParseRequestValue() {
        return ParseRequestValue;
    }

    public void setParseRequestValue(List parseRequestValue) {
        this.ParseRequestValue = parseRequestValue;
    }

    public List getResponseParameter() {
        return ResponseParameter;
    }

    public void setResponseParameter(List responseParameter) {
        this.ResponseParameter = responseParameter;
    }

    public String getApplicationID() {
        return ApplicationID;
    }

    public void setApplicationID(String applicationID) {
        this.ApplicationID = applicationID;
    }
/*       key:测试行业->测试商家->测试应用::测试接口
            value:
            {
                "URL": "http://106.12.199.235:8080/doPost",
                    "ReturnParasType": "",
                    "ParasType": "string_KeyValue",
                    "BufferType": "close",
                    "ParasOrder": "method#sceneName#accNum#prodType",
                    "NameSpace": "",
                    "CallType": "Http-post",
                    "Inner2CalledParasMap": [
                "method<-method",
                        "prodType<-prodType",
                        "accNum<-accNum",
                        "sceneName<-sceneName"
        ],
                "CalledRes2InnerResParas": [
                "交易记录->交易记录"
        ]
            }
     */


}
