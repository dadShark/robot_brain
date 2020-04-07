package com.robot_brain.nlu.flow.bean;

import com.sun.xml.internal.bind.v2.runtime.reflect.Lister;

import java.util.*;

public class OutsideApiInfo {


    /**
     * @ClassName OutsideApiInfo
     * @Description 第三方接口表中的配置信息
     * @Author waterkingko
     * @Date 2020年3月19日 00:35:42
     * @Version V1.0

     **/
    private String InterfaceName;//接口名称
    private String Business;//商家
    private String InterfaceAddress;//接口地址
    private String CallingMethod;//调用方式（HTTP，WEBSERVICE）
    private String RequestMethod;//（调用方式为http时，为GET、POST；调用方式为webservice时，为函数名称）
    private String NameSpace;//命名空间 调用方式为webservice时填写
    private Map<String,String> ParseRequestValue;//请求参数 多个参数名称以,连接
    private Map<String,String> ResponseParameter;//响应参数 多个参数名称以,连接
    private Map<String,String> CallReference;//节点入参
    private Map<String,String> InterParas;//格式化入参结果
    private String ApplicationID;//应用ID

    public OutsideApiInfo() {
        InterfaceName = "";
        Business = "";
        ParseRequestValue = new HashMap<String, String>();
        ResponseParameter = new HashMap<String, String>();
        RequestMethod = "";
        CallReference=new HashMap<String, String>();
        InterParas=new HashMap<String, String>();
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
            this.CallReference =apiInfo.CallReference;
            this.InterParas = apiInfo.InterParas;

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

    public Map<String, String> getParseRequestValue() {
        return ParseRequestValue;
    }

    public void setParseRequestValue(Map parseRequestValue) {
        this.ParseRequestValue = parseRequestValue;
    }

    public Map<String, String> getResponseParameter() {
        return ResponseParameter;
    }

    public void setResponseParameter(Map responseParameter) {
        this.ResponseParameter = responseParameter;
    }

    public String getApplicationID() {
        return ApplicationID;
    }

    public void setApplicationID(String applicationID) {
        this.ApplicationID = applicationID;
    }

    public Map<String, String> getCallReference() {
        return CallReference;
    }

    public void setCallReference(Map<String, String> callReference) {
        CallReference = callReference;
    }

    public Map<String, String> getInterParas() {
        return InterParas;
    }

    public void setInterParas(Map<String, String> interParas) {
        InterParas = interParas;
    }

    public void formatInParas(Map<String, String> requestvalue, Map<String, String> maps) {

        Map<String,String> inparas = new HashMap<String, String>();
        for (String key:requestvalue.keySet()) {
            String value =requestvalue.get(key);
            if (maps.containsKey(value)){
                String v= maps.get(value);
                inparas.put(key,v);
            }
        }
        setInterParas(inparas);

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
