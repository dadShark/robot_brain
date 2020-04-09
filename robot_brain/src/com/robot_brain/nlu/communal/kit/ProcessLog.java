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

    /*
     * description: 设置INFO级别日志
     * create by: shark
     * create time: 2020/3/23 14:14
     * @params [log]
     * @return void
     */
    protected void setLogInfo(String log) {
        String pattern="HH:mm:ss.SSS";
        SimpleDateFormat df = new SimpleDateFormat(pattern);// 设置日期格式
        String time = df.format(new Date());
        this.log += time+"INFO["+this.getClass().getName()+"]"+log+"\r\n";
    }

    /*
     * description: 设置Debug级别的日志
     * create by: shark
     * create time: 2020/3/23 14:15
     * @params [log]
     * @return void
     */
    protected void setLogDebug(String log) {
        String pattern="HH:mm:ss.SSS";
        SimpleDateFormat df = new SimpleDateFormat(pattern);// 设置日期格式
        String time = df.format(new Date());
        this.log += time+"DEBUG["+this.getClass().getName()+"]"+log+"\r\n";
    }

    /*
     * description: 在末尾追加日志
     * create by: shark
     * create time: 2020/3/23 14:14
     * @params [log]
     * @return void
     */
    public  void  addLog(String log){
        this.log+=log;
    }

}
