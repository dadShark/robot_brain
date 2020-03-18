package com.robot_brain.nlu.communal.kit;

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
    private String log="";

    public String getLog() {
        return log;
    }

    protected void setLogInfo(String log) {
        String pattern="HH:mm:ss.SSS";
        SimpleDateFormat df = new SimpleDateFormat(pattern);// 设置日期格式
        String time = df.format(new Date());
        this.log += time+"INFO["+this.getClass().getName()+"]"+log+"\r\n";
    }

    protected void setLogDebug(String log) {
        String pattern="HH:mm:ss.SSS";
        SimpleDateFormat df = new SimpleDateFormat(pattern);// 设置日期格式
        String time = df.format(new Date());
        this.log += time+"DEBUG["+this.getClass().getName()+"]"+log+"\r\n";
    }

}
