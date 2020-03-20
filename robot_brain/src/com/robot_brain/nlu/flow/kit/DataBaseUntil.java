package com.robot_brain.nlu.flow.kit;

import javax.servlet.jsp.jstl.sql.Result;
import javax.servlet.jsp.jstl.sql.ResultSupport;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @ClassName DataBaseUntil
 * @Description 数据库查询工具类
 * @Author waterkingko
 * @Date 2020年3月19日 22:58:17
 * @Version V1.0
 **/
public class DataBaseUntil {
    private static boolean isToMysql;//是否使用MySQL

    /**
 * @description 返回带参数 sql Result
 * @param sql
 * @return
 * @returnType Result
 */
    public static Result executedSQL(String sql){
        Connection connect = null;
        PreparedStatement parstmt = null;
        ResultSet res = null;
        Result rlt = null;
        //判断使用什么数据库

        if (rlt != null)
            return rlt;

        if (isToMysql) {
            MysqlTransfer mt = new MysqlTransfer(sql, null);
            mt.transfer();
            sql = mt.getMysqlSql();
            GlobalValue.myLog.debug(mt.toString());
        }

        try {
            connect = createCon();
            parstmt = connect.createStatement();
            res = parstmt.executeQuery(sql);
            if (res != null) {

                rlt = ResultSupport.toResult(res);

            }
        } catch (SQLException e) {
            GlobalValue.myLog.error("异常sql==>" + sql + " 异常信息==>", e);
        } finally {
            close(res);
            close(parstmt);
            close(connect);
        }

        return rlt;
    }

    /**
     * 描述：@description 获取数据库连接
     * 参数：@return
     * 返回值类型：@returnType Connection
     * 创建时间：@dateTime 2020年3月20日 15:30:36
     * 作者：@author waterkingko
     */
    public static Connection createCon() {
        Connection con = null;
        String isUsedataPool = GenericUntil
                .getGlobalProfileInfo("isUsedataPool");
        if ("".equals(isUsedataPool) || isUsedataPool == null) {
            return null;
        } else {
            if ("yes".equalsIgnoreCase(isUsedataPool)) {// 使用数据库连接池
                try {
                    con = dataSource.getConnection();
                } catch (SQLException e) {
                    GlobalValue.myLog.error(e);
                }
            } else {// 使用数据库普通连接
                try {
                    String driver = getCommmonLibJDBCValues("driverClassName"); // 数据库驱动
                    String url = getCommmonLibJDBCValues("url");// 连接地址
                    String user = getCommmonLibJDBCValues("username");// 用户名
                    String password = DESBase64
                            .decryptStringBase64(getCommmonLibJDBCValues("password"));// 密码
                    Class.forName(driver); // 加载数据库驱动
                    con = DriverManager.getConnection(url, user, password);
                } catch (Exception e) {
                    GlobalValue.myLog.error("【获取数据库连接异常】", e);
                    con = null;
                }
            }
        }
        return con;
    }



}
