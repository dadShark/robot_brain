package com.robot_brain.nlu.flow.outsideApiCaller;


import com.alibaba.fastjson.JSON;
import com.robot_brain.nlu.flow.kit.HTTPUntil;
import net.sf.json.JSONObject;
import com.robot_brain.nlu.communal.myInterface.StandardModule;
import com.robot_brain.nlu.flow.bean.OutsideApiInfo;
import com.robot_brain.nlu.flow.kit.GenericUntil;
import com.robot_brain.nlu.flow.kit.OutsideApiDAO;
import com.robot_brain.nlu.flow.kit.RedisUntil;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.rpc.client.RPCServiceClient;
import org.apache.axis2.client.Options;
import javax.xml.namespace.QName;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;

import javax.servlet.jsp.jstl.sql.Result;
import java.util.*;


public class OutsideApiCaller implements StandardModule {
    public void _init_() {
        //思路:查出答案之后直接将答案往redis中存。每次存之前清空对应KEY下的内容。
        Result dt = null;
        Map<String, String> redismap = null;
        try {
            dt = OutsideApiDAO.select("flow_interface_t");//第三方接口信息配制
            if (dt == null) {
                GenericUntil.error("LoadTheThirdInterfaceInfo(第三方接口信息配制)中获取接口参数输出配制表信息为空！！！");//日志输出函数·暂定
                return;
            }
        } catch (Exception e) {
            GenericUntil.myLog.error("(第三方接口信息配制)中获取接口参数输出配制表信息为空！！！");
        }
        if (dt != null) {
            for (Map<String, String> row : dt.getRows()) {
                OutsideApiInfo apiInfo = new OutsideApiInfo();
                String mbusiness = row.get("Business").toString().trim();//商家
                String minterface = row.get("InterfaceName").toString().trim();//接口名称
                String maddress = row.get("InterfaceAddress").toString().trim();//接口地址
                String mcallmethod = row.get("CallingMethod").toString().trim();//调用方式（HTTP，WEBSERVICE）
                String mrequestmethod = row.get("RequestMethod").toString().trim();//调用方式（HTTP，WEBSERVICE）
                String mnamespace = row.get("NameSpace").toString().trim();//命名空间 调用方式为webservice时填写
                String mrequestvalue = row.get("ParseRequestValue").toString().trim();//请求参数 多个参数名称以,连接
                String mreponsevalue = row.get("ResponseParameter").toString().trim();//响应参数 多个参数名称以,连接
                String mid = row.get("ApplicationID").toString().trim();//应用ID
                List mrequestlist;
                List mreponselist;
                if (mrequestvalue != null && mrequestvalue != "") {
                    mrequestlist = Arrays.asList(mrequestvalue.split(","));
                } else {
                    mrequestlist = null;
                }
                if (mreponsevalue != null && !mreponsevalue.equals(",")) {
                    mreponselist = Arrays.asList(mreponsevalue.split(","));
                } else {
                    mreponselist = null;
                }
                apiInfo.setInterfaceAddress(maddress);
                apiInfo.setCallingMethod(mcallmethod);
                apiInfo.setRequestMethod(mrequestmethod);
                apiInfo.setNameSpace(mnamespace);
                apiInfo.setParseRequestValue(mrequestlist);
                apiInfo.setResponseParameter(mreponselist);
                apiInfo.setBusiness(mbusiness);
                apiInfo.setApplicationID(mid);
                String jsonObject = JSONObject.fromObject(apiInfo).toString();

                String mkey = mbusiness + "::" + minterface;
                redismap.put(mkey, jsonObject);

            }
            try {
                RedisUntil.setOutSideApiReids(redismap);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("redis存入第三方接口失败");
            }

        }
    }
    /*
     */
    public String _main_(String outsideApiName) {
        String result= null;
        Map<String,String> maps = null;
        String mkey ="config:outsidApiInfo";
        //从redis中获取数据
        String business =GenericUntil.getGlobalProfileInfo("business");
        String keys = business+"::"+outsideApiName;
        maps = RedisUntil.getOutSideApiRedis(mkey);
        //从第三方接口表中查到对应接口
        Map outsideApiMaps = JSON.parseObject(maps.getOrDefault(outsideApiName,null));
        if (result ==null&&outsideApiMaps.containsKey("请求方式")){
            if (outsideApiMaps.get("请求方式") == "Get"){
                result = useHttpGET(outsideApiMaps);
                return result;
            }
            if (outsideApiMaps.get("请求方式") == "Post"){
                result = useHttpPost(outsideApiMaps);
                return result;
            }

            if (outsideApiMaps.get("请求方式") == "webservice"){
                useWebService(outsideApiMaps);
                return result;
            }
        }
        //根据第三方配置名从redis中将json串取出到map中
        //判断map中元素invocation_way判断使用哪种解析接口方式HTTP，WEBSERVICE
       return result;
    }

    private String useHttpPost(Map<String, String> outsideApiMaps) {
        Map<String, String> resMap = new HashMap<String, String>();
        String url = outsideApiMaps.get("interfaceAddress");
        Object[] paras = outsideApiMaps.get("paras").split(",");
        System.out.println("【第三方接口服务调用入参】：" + url);

        long t1 = System.currentTimeMillis();
        String json = HTTPUntil.post(url, JSON.toJSONString(paras)
                .toString());
        long t2 = System.currentTimeMillis();
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(t2 - t1);
        String time = c.get(Calendar.SECOND) + "秒 "
                + c.get(Calendar.MILLISECOND) + " 毫秒";

        if (json != null && json.length() > 0) {
            //GlobalValue.myLog.info("【第三方接口返回结果】" + json);

            JSONObject jsObj = JSONObject.fromObject(json);
            resMap = (Map<String, String>) JSONObject.toBean(jsObj,
                        Map.class);
        }
        return resMap.toString();
    }

    private String useWebService(Map<String, String> outsideApiMaps) {
        Map<String, String> resMap = new HashMap<String, String>();
        RPCServiceClient serviceClient = null;
        System.out.println("进入webservice调用");
        try {
            // 客户端控件
            serviceClient = new RPCServiceClient();
            Options options = serviceClient.getOptions();
            options.setTimeOutInMilliSeconds(40000L);
            String url = outsideApiMaps.get("interfaceAddress");
            EndpointReference goalER = new EndpointReference(url);
            options.setTo(goalER);
            // 命名空间
            QName qName = new QName(outsideApiMaps.get("NameSpace"), outsideApiMaps.get("CallFuncName"));
            // 返回值的类型，基本类型为
            Class<?>[] returnType = new Class[] { String.class };
            // 调用方法传入参数
            Object[] paras = outsideApiMaps.get("parseRequestValue").split(",");
            long t1 = System.currentTimeMillis();

            Object[] result = null;
            if (paras.length > 0) {
                System.out.println("【第三方接口服务调用入参】：" + paras[0].toString());
            }

            result = serviceClient.invokeBlocking(qName, paras, returnType);
            // 新增的转义代码
            if (result != null && result.length != 0 && result[0] != null) {
                if (result[0].toString().contains("&amp;")) {
                    result[0] = result[0].toString().replace("&amp;", "&");
                }
            }
            long t2 = System.currentTimeMillis();
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(t2 - t1);
            String time = c.get(Calendar.SECOND) + "秒 "
                    + c.get(Calendar.MILLISECOND) + " 毫秒";
            if (result != null && result.length > 0 && result[0] != null) {
                if (outsideApiMaps.get("").equalsIgnoreCase(
                        "string_KeyValue")
                        || outsideApiMaps.get("").equalsIgnoreCase(
                        "Json_KeyValue")) {
                    JSONObject jsObj = JSONObject.fromObject(result[0]);
                    resMap = (Map<String, String>) JSONObject.toBean(jsObj,
                            Map.class);
                } else if (outsideApiMaps.get("").equals("list")) {
                    String strCat = "";
                    for (int i = 0; i < result.length; i++) {
                        strCat += result[i].toString();
                        strCat += "#";
                    }
                    resMap.put("空", strCat);
                } else if (outsideApiMaps.get("").equalsIgnoreCase("String")) {
                    resMap.put("空", result[0].toString());
                }
            }
            return resMap.toString();
        } catch (Exception e) {
            //GlobalValue.myLog.error(info.getURL() + "调用异常：", e);
            System.out.println(e.toString());
            return resMap.toString();
            // return "";
        } finally {
            try {
                serviceClient.cleanupTransport();
            } catch (Exception e) {
                //GlobalValue.myLog.error(e);
            }
        }
        return null;
    }

    private String useHttpGET(Map<String, String> outsideApiMaps) {
        // http://km.knowology.cn:8082/CommonDataCount/CDC?params={"用户反馈":"未解决","联系方式":"手机号","地市":"","姓名":"zch","用户id":"A47F3BBDC38D0A417F409D336A8B6F9D","省份":"","反馈内容":"sssss","评论时间":"2019/01/28 30:08:05","对话id":"A47F3BBDC38D0A417F409D336A8B6F9D","用户咨询":"赎回到账时间","联系号码":"18362218921","业务渠道":"Web","商家":"证券行业->东方证券->多渠道应用"}
        Map<String, String> resMap = new HashMap<String, String>();
        String url = outsideApiMaps.get("madress") + "?";
        String params = outsideApiMaps.get("mxxx");
        url += params;
        long t1 = System.currentTimeMillis();
        String json = HTTPUntil.get(url);
        long t2 = System.currentTimeMillis();
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(t2 - t1);
        String time = c.get(Calendar.SECOND) + "秒 "
                + c.get(Calendar.MILLISECOND) + " 毫秒";
        if (json != null && json.length() > 0) {
            JSONObject jsObj = JSONObject.fromObject(json);
            resMap = (Map<String, String>) JSONObject.toBean(jsObj,
                        Map.class);

        }
        return resMap.toString();

    }


}
