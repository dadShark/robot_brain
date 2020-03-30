package com.robot_brain.nlu.flow.outsideApiCaller;


import com.alibaba.fastjson.JSON;
import net.sf.json.JSONObject;
import com.robot_brain.nlu.communal.myInterface.StandardModule;
import com.robot_brain.nlu.flow.bean.OutsideApiInfo;
import com.robot_brain.nlu.flow.kit.GenericUntil;
import com.robot_brain.nlu.flow.kit.OutsideApiDAO;
import com.robot_brain.nlu.flow.kit.RedisUntil;
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
            if (outsideApiMaps.get("请求方式") == "HTTP"){
                result = useHTTP(outsideApiMaps);
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

    private String useWebService(Map<String, String> maps) {
        return null;
    }

    private String useHTTP(Map<String, String> maps) {

        return null;
    }


}
