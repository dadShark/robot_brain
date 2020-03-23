package com.robot_brain.nlu.flow.outsideApiCaller;

import com.robot_brain.nlu.communal.myInterface.StandardModule;
import com.robot_brain.nlu.flow.bean.OutsideApiInfo;
import com.robot_brain.nlu.flow.kit.GenericUntil;
import com.robot_brain.nlu.flow.kit.OutsideApiDAO;
import com.robot_brain.nlu.flow.kit.RedisUntil;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;

import javax.servlet.jsp.jstl.sql.Result;
import java.util.ArrayList;
import java.util.Map;
import java.util.SortedMap;


public class OutsideApiCaller implements StandardModule {
    public void _init_() {
        //思路:查出答案之后直接将答案往redis中存。每次存之前清空对应KEY下的内容。
        Result dt = null;
        try {
            dt = OutsideApiDAO.select("flow_interface_t");//第三方接口信息配制
            if (dt == null) {
                GenericUntil.error("LoadTheThirdInterfaceInfo(第三方接口信息配制)中获取接口参数输出配制表信息为空！！！");//日志输出函数·暂定
                return;
            }
        } catch (Exception e) {
            GenericUntil.myLog.error("(第三方接口信息配制)中获取接口参数输出配制表信息为空！！！");
        }
        if (dt != null){
            for (Map<String, String> row : dt.getRows()) {
                String mbusiness = row.get("mbusiness").toString().trim();
                String mApp = row.get("mApp").toString().trim();
                String mname = row.get("mname").toString().trim();
                RedisUntil.setOutSideApiReids(mbusiness, mApp, mname, row);

            }
        }
    }

    /*
    *
    *
    *
     */
    public String _main_(String business,String apps,String names) {
        String result= null;
        //从redis中获取数据

        Map<String,String> maps = null;
        maps = RedisUntil.getOutSideApiRedis(business,apps,names);
        //根据
        if (result ==null&&maps.containsKey("请求方式")){
            if (maps.get("请求方式") == "HTTP"){
                result = useHTTP(maps);
                return result;
            }
            if (maps.get("请求方式") == "webservice"){
                useWebService(maps);
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
