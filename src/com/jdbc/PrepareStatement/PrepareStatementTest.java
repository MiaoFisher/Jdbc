package com.jdbc.PrepareStatement;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class PrepareStatementTest {
    @Test
    public void testInsert() {
        //创建Connection对象
        Connection connection = null;
        //创建PrepareStatement对象
        PreparedStatement ps = null;
        try {
            //根据配置文件获取配置信息
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("jdbc.properties");
            Properties properties = new Properties();
            properties.load(inputStream);
            //获取基本信息
            String user = properties.getProperty("user");
            String url = properties.getProperty("url");
            String password = properties.getProperty("password");
            String driverClass = properties.getProperty("driverClass");
            //加载Driver对象
            Class clazz = Class.forName(driverClass);
            //获取连接对象
            connection = DriverManager.getConnection(url, user, password);
            //根据Connection对象获得PrepareStatement对象
            String sql = "insert into customers(name,email,birth) values (?,?,?)";
            ps = connection.prepareStatement(sql);
            //填充占位符符号，注意：index是从1开始
            ps.setString(1,"超越");
            ps.setString(2,"chaoyue@qq.com");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse("1998-7-31");
            ps.setDate(3,new java.sql.Date(date.getTime()));
            //执行sql语句
            ps.execute();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //关闭流
            if (ps == null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection == null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
