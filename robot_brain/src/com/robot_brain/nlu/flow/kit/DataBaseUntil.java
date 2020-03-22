package com.robot_brain.nlu.flow.kit;

import javax.servlet.jsp.jstl.sql.Result;
import javax.servlet.jsp.jstl.sql.ResultSupport;
import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static com.robot_brain.nlu.flow.kit.GenericUntil.myLog;

/**
 * @ClassName DataBaseUntil
 * @Description 数据库查询工具类
 * @Author waterkingko
 * @Date 2020年3月19日 22:58:17
 * @Version V1.0
 **/
public class DataBaseUntil {
    /**
     * 定义全局 dataSource
     */
    private static DataSource dataSource;
    private static boolean isToMysql;//是否使用MySQL

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
//        /
//        if (isToMysql) {
//            MysqlTransfer mt = new MysqlTransfer(sql, null);
//            mt.transfer();
//            sql = mt.getMysqlSql();
//            //GlobalValue.myLog.debug(mt.toString());
//        }

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
        String isUsedataPool = GenericUntil
                .getGlobalProfileInfo("isUsedataPool");
        if ("".equals(isUsedataPool) || isUsedataPool == null) {
            return null;
        } else {
            if ("yes".equalsIgnoreCase(isUsedataPool)) {// 使用数据库连接池
                try {
                    con = dataSource.getConnection();
                } catch (SQLException e) {
                    //GlobalValue.myLog.error(e);
                }
            } else {// 使用数据库普通连接
                try {
                    String driver = getGlobalDeployJDBCValues("driverClassName"); // 数据库驱动
                    String url = getGlobalDeployJDBCValues("url");// 连接地址
                    String user = getGlobalDeployJDBCValues("username");// 用户名
                    String password = DataEncryption
                            .decryptStringBase64(getGlobalDeployJDBCValues("password"));// 密码
                    Class.forName(driver); // 加载数据库驱动
                    con = DriverManager.getConnection(url, user, password);
                } catch (Exception e) {
                    myLog.error("【获取数据库连接异常】", e);
                    con = null;
                }
            }
        }
        return con;
    }

    /**
     * 描述：@description 读取jdbc参数 参数：@param key 参数：@return value 返回值类型：@returnType
     * String 创建时间：@dateTime 2015-9-21下午01:44:52 作者：@author wellhan
     */
    public static String getGlobalDeployJDBCValues(String key) {
        String conObject = GenericUntil.getCommmonLibGlobalValues("connectFrom");
        if ("".equals(conObject) || conObject == null) {
            return null;
        }
        conObject = conObject.replace(" ", "");
        if (isToMysql)
            conObject = "mysql";
        String jdbcProPath = "jdbc_" + conObject;
        try {
            // ResourceBundle resourcesTable = ResourceBundle
            // .getBundle(jdbcProPath);
            // return resourcesTable.getString(key);
            return GenericUntil.getXMLInfo(key, jdbcProPath);
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
            myLog.error(ex);
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
            GenericUntil.myLog.error("关闭连接异常信息==>", ex);
        }
    }

}
