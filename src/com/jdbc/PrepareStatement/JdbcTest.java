package com.jdbc.PrepareStatement;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
//获取数据库连接的方式
public class JdbcTest {
    //方式1
    @Test
    public void testConnection1() throws SQLException {
        //先创建驱动对象（获取Driver类实现对象）
        Driver driver = new com.mysql.cj.jdbc.Driver();
        //设置 url(数据库所在位置)， 和properties(用于配置数据库的用户名和密码)
        String url = "jdbc:mysql://localhost:3306/test?characterEncoding=utf8&useSSL=false&serverTimezone=UTC&rewriteBatchedStatements=true";
        Properties info = new Properties();
        //设置用户名和密码
        info.setProperty("user","root");
        info.setProperty("password","123456");
        //获得连接对象Connection
        Connection connection = driver.connect(url,info);
        System.out.println(connection);
    }
    //方式二：使用反射的方式，使得程序可以不出现第三方软件，使得程序具有更好的移植性
    @Test
    public void testConnection2() throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        //1.获得Driver实现类，使用反射
        Class clazz = Class.forName("com.mysql.cj.jdbc.Driver");
        Driver driver = (Driver) clazz.newInstance();
        //设置 url(数据库所在位置)， 和properties(用于配置数据库的用户名和密码)
        String url = "jdbc:mysql://localhost:3306/test?characterEncoding=utf8&useSSL=false&serverTimezone=UTC&rewriteBatchedStatements=true";
        Properties info = new Properties();
        //设置用户名和密码
        info.setProperty("user","root");
        info.setProperty("password","123456");
        //获得连接对象Connection
        Connection connection = driver.connect(url,info);
        System.out.println(connection);
    }
    //方式三：使用DriverManger
    @Test
    public void testConnection3() throws IllegalAccessException, InstantiationException, ClassNotFoundException, SQLException {
        //1.获得Driver实现类
        Class clazz = Class.forName("com.mysql.cj.jdbc.Driver");
        Driver driver = (Driver) clazz.newInstance();
        //2.提供基本信息
        String user = "root";
        String password = "123456";
        String url = "jdbc:mysql://localhost:3306/test?characterEncoding=utf8&useSSL=false&serverTimezone=UTC&rewriteBatchedStatements=true";
        //3.注册驱动
        DriverManager.registerDriver(driver);
        //4.通过DriverManger获得连接对象Connection
        Connection connection = DriverManager.getConnection(url,user,password);
        System.out.println(connection);
    }
    //方式四：在方法三的基础上，减少了获取驱动步骤（只是在代码中）
    @Test
    public void testConnection4() throws ClassNotFoundException, SQLException {
        //1.获得基本信息
        String user = "root";
        String password = "123456";
        String url = "jdbc:mysql://localhost:3306/test?characterEncoding=utf8&useSSL=false&serverTimezone=UTC&rewriteBatchedStatements=true";

        //2.加载Driver
        Class clazz = Class.forName("com.mysql.cj.jdbc.Driver");
        //相比于步骤三，可以省略如下步骤
        //Driver driver = (Driver) clazz.newInstance();
        //DriverManager.registerDriver(driver);
        //原因：在装载的时候 Driver里面有如下代码
//        static {
//            try {
//                DriverManager.registerDriver(new Driver());
//            } catch (SQLException var1) {
//                throw new RuntimeException("Can't register driver!");
//            }
//        }
        //已经帮助我们完成了驱动的注册
        //3.获得Connection连接对象
        Connection connection = DriverManager.getConnection(url,user,password);
        System.out.println(connection);
    }
    //方法5，使用配置文件的方式去存取基本信息
    @Test
    public void testConnection5() throws IOException, ClassNotFoundException, SQLException {
        //1.根据配置文件获取基本信息
        //1.1创建输入流获取对象
         InputStream is = JdbcTest.class.getClassLoader().getResourceAsStream("jdbc.properties");
         Properties properties = new Properties();
         //1.2将输入流装载到properties中
         properties.load(is);
         //1.3读取配置文件信息
        String user = properties.getProperty("user");
        String url = properties.getProperty("url");
        String password = properties.getProperty("password");
        String driverClass = properties.getProperty("driverClass");
        //2.1加载Driver
        Class clazz = Class.forName(driverClass);
        //3获取Connection连接对象
        Connection connection = DriverManager.getConnection(url,user,password);
        System.out.println(connection);
    }
}
