package com.jdbc.utils3;

import com.mchange.v2.c3p0.DataSources;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;
//使用DBCP数据库连接池
public class JDBCUtils {
    private static DataSource dataSource = null;
    static {
        //载入配置文件
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(new File("src/DBCP.properties"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Properties properties = new Properties();
        try {
            properties.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //根据配置文件创建数据源
        try {
            dataSource = BasicDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws Exception {
        return dataSource.getConnection();
    }
    @Test
    public void testConnection() throws Exception {
        System.out.println(getConnection());
    }
}
