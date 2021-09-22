package com.jdbc.DatabaseConnectionPool;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;
import org.junit.Test;

import java.beans.PropertyVetoException;
import java.sql.SQLException;
//测试c3p0数据库连接池
public class connectionTest {
    //手动配置文件信息
    @Test
    public void testConnection() throws PropertyVetoException, SQLException {
        ComboPooledDataSource cpds = new ComboPooledDataSource();
        cpds.setDriverClass( "com.mysql.cj.jdbc.Driver" ); //loads the jdbc driver
        cpds.setJdbcUrl( "jdbc:mysql://localhost:3306/test?characterEncoding=utf8&useSSL=false&serverTimezone=UTC&rewriteBatchedStatements=true" );
        cpds.setUser("root");
        cpds.setPassword("123456");
        System.out.println(cpds.getConnection());
        //销毁数据库连接
        DataSources.destroy(cpds);
    }
    //使用配置文件
    //在src下写配置文件c3p0-config.xml，在创建Cpds的时候写入configName,就会自动读取文件配置信息
    @Test
    public void testConnection2() throws SQLException {
        ComboPooledDataSource cpds = new ComboPooledDataSource("c3p0Configuration");
        System.out.println(cpds.getConnection());
        DataSources.destroy(cpds);
    }
}
