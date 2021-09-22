package com.jdbc.utils2;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 通过c3p0数据库连接池获得连接
 */
public class JDBCUtils {
    //先在方法外面创建数据库连接池
    //因为这个对象只需要创建一次就可以了
    private static ComboPooledDataSource cpds = new ComboPooledDataSource("c3p0Configuration");
    //返回数据库连接
    public static Connection getConnection() throws SQLException {
        return cpds.getConnection();
    }
    @Test
    public void testConnection() throws SQLException {
        System.out.println(getConnection());
    }
}
