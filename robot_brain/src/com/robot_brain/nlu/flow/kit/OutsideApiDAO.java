package com.robot_brain.nlu.flow.kit;

import javax.servlet.jsp.jstl.sql.Result;

public class OutsideApiDAO {

    public static Result select(String outsiderinfo){
        try{
            String sql = "select " +
                    "application_id,interface_name,interface_url,invocation_way," +
                    "function_name,namespace,req_param,resp_param " +
                    "from "+outsiderinfo+"where remove = 0";
            return DataBaseUntil.executedSQL(sql)
        }catch(Exception e)
        {
            GlobalValue.myLog.error("【select】",e);
            return null;
        }
    }
}
