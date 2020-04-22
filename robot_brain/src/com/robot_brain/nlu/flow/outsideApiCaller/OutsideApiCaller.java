package com.robot_brain.nlu.flow.outsideApiCaller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.robot_brain.nlu.communal.myInterface.StandardModule;
import com.robot_brain.nlu.flow.bean.OutsideApiInfo;
import com.robot_brain.nlu.flow.kit.GenericUntil;
import com.robot_brain.nlu.flow.kit.HTTPUntil;
import com.robot_brain.nlu.flow.kit.OutsideApiDAO;
import com.robot_brain.nlu.flow.kit.RedisUntil;

import javax.servlet.jsp.jstl.sql.Result;
import java.util.HashMap;
import java.util.Map;

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
                Map<String,String> mrequestlist =new HashMap<String, String>();
                Map<String,String> mreponselist =new HashMap<String, String>();
                if (mrequestvalue != null && mrequestvalue != "") {
                    String[] mrequestarry = mrequestvalue.split(",");
                    for (String values:mrequestarry) {
                        if (values.contains("<-")){
                            int q =values.indexOf("<-");
                            if(q == -1){
                                continue;
                            }
                            String keys = values.substring(0,q);
                            String v =values.substring(q+2,-1);
                            mrequestlist.put(keys,v);
                        }

                    }
                }
                if (mreponsevalue != null && mreponsevalue != "") {
                    String[] mrequestarry = mreponsevalue.split(",");
                    for (String values:mrequestarry) {
                        if (values.contains("->")){
                            int q =values.indexOf("->");
                            if(q == -1){
                                continue;
                            }
                            String keys = values.substring(0,q);
                            String v =values.substring(q+2,-1);
                            mreponselist.put(keys,v);
                        }

                    }
                }
                apiInfo.setInterfaceAddress(maddress);
                apiInfo.setCallingMethod(mcallmethod);
                apiInfo.setRequestMethod(mrequestmethod);
                apiInfo.setNameSpace(mnamespace);
                apiInfo.setParseRequestValue(mrequestlist);
                apiInfo.setResponseParameter(mreponselist);
                apiInfo.setBusiness(mbusiness);
                apiInfo.setApplicationID(mid);
                String json = JSON.toJSONString(apiInfo);

                String mkey = mbusiness + "::" + minterface;
                redismap.put(mkey, json);

            }
            try {
                RedisUntil.setOutSideApiReids(redismap);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("redis存入第三方接口失败");
            }

        }
    }


    public String _main_(Map<String,String> maps) {
        Map<String,String> result= null;
        String rlt="";
        //从maps中获获取商家和应用
        String apiname = maps.get("信息获取");
        if (maps.containsKey("用户类型")){
            switch (maps.get("用户类型")){
                case "个人":
                    maps.put("用户类型","0");
                    break;
                case "单位":
                    maps.put("用户类型","1");
                    break;
            }

        }
        if(apiname.equals("查询单位名下车牌号")){
            apiname="查询个人名下车牌号";
        }
        if (maps.containsKey("信息获取")){
            switch (maps.get("信息获取")){
                case "查询个人名下车牌号":
                    maps.put("funcation","1");
                    break;
                case "查询车牌对应的ETC卡号":
                    maps.put("funcation","2");
                    break;
                case "查询通行明细":
                    maps.put("funcation","3");
                    break;
                case "查询单位名下车牌号":
                    maps.put("funcation","1");
                    break;
            }

        }
        String businessid = maps.get("商家ID");
        //合成key
        String mkey =businessid+"::"+apiname;
        //从redis中将第三方接口信息全部读取出来
        Map<String,String> getRedisConfig = RedisUntil.getOutSideApiRedis("config:outsidApiInfo");

        //通过key将对应商家的对应第三方接口信息读取出来
        String mvalue = getRedisConfig.getOrDefault(mkey,null);
        try{
            //反序列化为OutsideApiInfo数据结构
            JSONObject objects = JSON.parseObject(mvalue);
            //将数据结构转成javaBean结构
             OutsideApiInfo outsideApiMaps = JSON.toJavaObject(objects,OutsideApiInfo.class);
            //将map中的数据通过请求参数信息ParseRequestValue读取成对应的key与value的map存入格式化入参结果InterParas中;
            Map<String,String> requestValue =outsideApiMaps.getParseRequestValue();
            Map<String,String> responseParameter =outsideApiMaps.getResponseParameter();
            outsideApiMaps.formatInParas(requestValue,maps);
            if (result == null&&outsideApiMaps.getCallingMethod().equals("HTTP")){
                if (outsideApiMaps.getRequestMethod().equals("GET")){
                    result = useHttpGET(outsideApiMaps);
                    if (result!=null){
                        for (String rs:result.keySet()){
                            //{"car_id":"","car_id_num":"0"}
                            String value=result.getOrDefault(rs,"空");
                            if (responseParameter.containsKey(rs)){
                                String rekey = responseParameter.getOrDefault(rs,"无");
                                maps.put(rekey,value);
                                if (value!=""){
                                    rlt+=rekey+"为"+value+".";
                                }else{
                                    rlt+=rekey+"为无.";
                                }

                            }
                        }
                    }
                }
//                if (outsideApiMaps.getRequestMethod().equals("POST")){
//                    result = useHttpPost(outsideApiMaps);
//                    return result;
//                }
            }
//            if (result == null&&outsideApiMaps.getCallingMethod().equals("webservice")){
//
//                result = useWebService(outsideApiMaps);
//                return result;
//
//            }

        }catch (Exception e){
            e.printStackTrace();
        }


//        String mkey ="config:outsidApiInfo";
//        //从redis中获取数据
//        String business =GenericUntil.getGlobalProfileInfo("business");
//        String keys = business+"::"+outsideApiName;
//        maps = RedisUntil.getOutSideApiRedis(mkey);
//        //从第三方接口表中查到对应接口
//        Map outsideApiMaps = JSON.parseObject(maps.getOrDefault(keys,null));

        //根据第三方配置名从redis中将json串取出到map中
        //判断map中元素invocation_way判断使用哪种解析接口方式HTTP，WEBSERVICE
        return rlt;
    }


    private Map<String, String> useHttpGET(OutsideApiInfo outsideApiMaps) {
        // http://km.knowology.cn:8082/CommonDataCount/CDC?params={"用户反馈":"未解决","联系方式":"手机号","地市":"","姓名":"zch","用户id":"A47F3BBDC38D0A417F409D336A8B6F9D","省份":"","反馈内容":"sssss","评论时间":"2019/01/28 30:08:05","对话id":"A47F3BBDC38D0A417F409D336A8B6F9D","用户咨询":"赎回到账时间","联系号码":"18362218921","业务渠道":"Web","商家":"证券行业->东方证券->多渠道应用"}
        //Map<String, String> resMap = new HashMap<String, String>();
        String url = outsideApiMaps.getInterfaceAddress() + "?";
        Map<String,String> params = outsideApiMaps.getInterParas();

        for (String key:params.keySet()
        ) {
            url +=key+"="+params.get(key)+"&";
        }
        url = url.substring(0,url.length()-1);
        String json = HTTPUntil.get(url).replace("\n","");
        if (json != null && json.length() > 0) {
            JSONObject obj = JSON.parseObject(json);

            //resMap= (Map<String, String>)JSONObject.parse(json);
            Map<String, String> resMap =JSON.toJavaObject(obj,Map.class);
            return resMap;
        }else {
            Map<String, String> resMap = new HashMap<String, String>();
            resMap.put("结果","无");
            return resMap;
        }
    }

    private Map<String, String> useHttpPost(OutsideApiInfo outsideApiMaps) {
        Map<String, String> resMap = new HashMap<String, String>();
        String url = outsideApiMaps.getInterfaceAddress();
        String paras=null;
        System.out.println("【第三方接口服务调用入参】：" + url);


        String json = HTTPUntil.post(url, JSON.toJSONString(paras));
        // TODO: 2020/4/15
        return resMap;
    }

    private Map<String, String> useWebService(OutsideApiInfo outsideApiMaps) {
        Map<String, String> resMap = new HashMap<String, String>();
        // TODO: 2020/4/15
        return resMap;
    }
}