package com.robot_brain.nlu.bean;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName ProcessLog
 * @Description
 * @Author shark
 * @Date 2020/3/15 11:29
 * @Version V1.0
 **/
public class ProcessLog {
    String log="";

    public String getLog() {
        return log;
    }

    protected void setLog(String log) {
        String pattern="HH:mm:ss.SSS";
        SimpleDateFormat df = new SimpleDateFormat(pattern);// 设置日期格式
        String time = df.format(new Date());
        this.log += time+"【"+this.getClass().getName()+"】"+log+"\r\n";
    }


}
