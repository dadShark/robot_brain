package com.robot_brain.nlu.flow.bean;

public class OutsideApiInfo {


    /**
     * @ClassName OutsideApiInfo
     * @Description 第三方接口表中的配置信息
     * @Author waterkingko
     * @Date 2020年3月19日 00:35:42
     * @Version V1.0
     * @deprecated  暂时不用该方法
     **/
    private String interface_name;//接口信息名
    private String business;//商家
    private String Apiurl;//地址
    private String invocation_way;
    private String function_name;
    private String namespace;
    private String req_param;
    private String resp_param;
    private String application_id;

    public OutsideApiInfo(){
        interface_name = "";
        business = "";
        req_param = "";
        resp_param = "";
        function_name = "";
    }

    public OutsideApiInfo(OutsideApiInfo apiInfo ){
        if (apiInfo == null) {
            return;
        }
        try{
            this.interface_name =apiInfo.interface_name;
            this.Apiurl=apiInfo.Apiurl;
            this.business=apiInfo.business;
            this.function_name =apiInfo.function_name;
            this.invocation_way =apiInfo.invocation_way;
            this.namespace = apiInfo.namespace;
            this.req_param=apiInfo.req_param;
            this.resp_param=apiInfo.resp_param;
            this.application_id=apiInfo.application_id;

        }catch (Exception e){
            System.out.println("获取第三方接口配置出错");
        }
    }

    public String getInterface_name() {
        return interface_name;
    }

    public void setInterface_name(String interface_name) {
        this.interface_name = interface_name;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public String getApiurl() {
        return Apiurl;
    }

    public void setApiurl(String apiurl) {
        Apiurl = apiurl;
    }

    public String getInvocation_way() {
        return invocation_way;
    }

    public void setInvocation_way(String invocation_way) {
        this.invocation_way = invocation_way;
    }

    public String getFunction_name() {
        return function_name;
    }

    public void setFunction_name(String function_name) {
        this.function_name = function_name;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getReq_param() {
        return req_param;
    }

    public void setReq_param(String req_param) {
        this.req_param = req_param;
    }

    public String getResp_param() {
        return resp_param;
    }

    public void setResp_param(String resp_param) {
        this.resp_param = resp_param;
    }

    public String getApplication_id() {
        return application_id;
    }

    public void setApplication_id(String application_id) {
        this.application_id = application_id;
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
