package com.jdbc.utils4;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.druid.util.JdbcUtils;
import com.mchange.v2.c3p0.DataSources;
import org.apache.commons.dbutils.DbUtils;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

//下面是使用德鲁伊数据库连接池技术获取数据库连接
public class JDBCUtils {
    //根据配置文件读取信息
    private static DataSource dataSource = null;
    static {
        Properties properties = new Properties();
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");
        try {
            properties.load(is);
            dataSource = DruidDataSourceFactory.createDataSource(properties);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
    @Test
    public void testConnection() throws SQLException {
        System.out.println(getConnection());
    }
    //关闭资源，这里之关闭Connection连接，因为其他的资源在查询结束后都会自动关闭
    public static void closeResource(Connection connection){
        DbUtils.closeQuietly(connection);

    }
}
