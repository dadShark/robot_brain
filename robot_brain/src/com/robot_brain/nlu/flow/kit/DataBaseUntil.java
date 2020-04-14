package com.robot_brain.nlu.flow.kit;

import javax.servlet.jsp.jstl.sql.Result;
import javax.servlet.jsp.jstl.sql.ResultSupport;
import java.sql.*;

import static com.robot_brain.nlu.flow.kit.GenericUntil.myLog;

public class DataBaseUntil {

    /**
     * @description 返回带参数 sql Result
     * @param sql
     * @return
     * @returnType Result
     */
    public static Result executedSQL(String sql){
        Connection connect = null;
        Statement parstmt = null;
        ResultSet res = null;
        Result rlt = null;


        if (rlt != null)
            return rlt;

        try {
            connect = createCon();
            parstmt = connect.createStatement();
            res = parstmt.executeQuery(sql);
            if (res != null) {

                rlt = ResultSupport.toResult(res);

            }
        } catch (SQLException e) {
            myLog.error("异常sql==>" + sql + " 异常信息==>", e);
        }catch (NullPointerException e) {
            System.out.println("出现空指针异常");
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
        try {
            String driver = getGlobalDeployJDBCValues("driverClassName"); // 数据库驱动
            String url = getGlobalDeployJDBCValues("url");// 连接地址
            String user = getGlobalDeployJDBCValues("username");// 用户名
            String password = getGlobalDeployJDBCValues("password");// 密码
            Class.forName(driver); // 加载数据库驱动
            con = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            con = null;
        }
        return con;
    }

    /**
     * 描述：@description 读取jdbc参数
     * 参数：@param key
     * 参数：@return value
     * 返回值类型：@returnType String
     * 创建时间：@dateTime 2015-9-21下午01:44:52
     * 作者：@author waterkingko
     */
    public static String getGlobalDeployJDBCValues(String key) {
        String conObject = GenericUntil.getGlobalProfileInfo("connectFrom");
        if ("".equals(conObject) || conObject == null) {
            return null;
        }
        conObject = conObject.replace(" ", "");
        String jdbckey = key + "_"+ conObject;
        try {
            return GenericUntil.getGlobalProfileInfo(jdbckey);
        } catch (Exception e) {
            myLog.error(e.getMessage());
            return "";
        }
    }


    protected static void close(ResultSet rs) {
        try {
            if (rs != null)
                rs.close();
        } catch (Exception ex) {
            myLog.error("关闭连接异常信息==>", ex);
        }
    }
    /**
     * @description 关闭 Statement
     * @param stmt
     * @returnType void
     */
    protected static void close(Statement stmt) {
        try {
            if (stmt != null)
                stmt.close();
        } catch (Exception ex) {
            // 写异常日志
            myLog.error("关闭连接异常信息==>", ex);
        }
    }

    /**
     * @description 关闭 Connection
     * @param con
     * @returnType void
     */
    protected static void close(Connection con) {
        try {
            if (con != null && !con.isClosed())
                con.close();
        } catch (Exception ex) {
            myLog.error("关闭连接异常信息==>", ex);
        }
    }
}
