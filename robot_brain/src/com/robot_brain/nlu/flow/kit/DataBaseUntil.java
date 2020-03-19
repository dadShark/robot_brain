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
/**
 * @description 返回带参数 sql Result
 * @param sql
 * @param obj
 * @return
 * @returnType Result
 */
    public static Result executedSQL(String sql,Object... obj){
        Connection connect = null;
        PreparedStatement parstmt = null;
        ResultSet res = null;
        Result rlt = null;
        //判断使用什么数据库
        if (getIsUseMemoryDB()) {
            rlt = getDataBaseCacheResult(sql);
        }
        if (rlt != null)
            return result;

        if (isToMysql) {
            MysqlTransfer mt = new MysqlTransfer(sql, null);
            mt.transfer();
            sql = mt.getMysqlSql();
            GlobalValue.myLog.debug(mt.toString());
        }

        try {
            connect = getCon();
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

        if (getIsUseMemoryDB() && null != rlt) {
            writeDataBaseCache(sql, rlt);
        }
        return rlt;
    }
}
