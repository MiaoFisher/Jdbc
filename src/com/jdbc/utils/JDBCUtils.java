package com.jdbc.utils;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.text.ParseException;
import java.util.Properties;

/**
 * @author mxs
 */
public class JDBCUtils {
    /**
     * 获取数据库连接对象
     * @return 返回一个连接对象Connection
     */
    public static Connection getConnection() throws IOException, ClassNotFoundException, SQLException {
        //1.根据配置文件获取基本信息
        //1.1创建输入流获取对象
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");
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
        //4.返回连接对象
        return connection;
    }

    /**
     * 关闭数据库连接
     * @param connection
     * @param ps
     */
    public static void closeResource(Connection connection, PreparedStatement ps){
        try {
            if (ps == null) {
                ps.close();
            }
            if (connection == null) {
                connection.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 关闭数据库连接
     * @param connection
     * @param ps
     */
    public static void closeResource(Connection connection, PreparedStatement ps,ResultSet rs){
        try {
            if (ps == null) {
                ps.close();
            }
            if (connection == null) {
                connection.close();
            }
            if (rs == null) {
                rs.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //关闭数据库资源（不包括Connection）
    public static void closeResource(PreparedStatement ps,ResultSet rs){
        try {
            if (ps == null) {
                ps.close();
            }
            if (rs == null) {
                rs.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void closeResource(Connection connection){
        if (connection == null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public static void closeResource(PreparedStatement ps){
        if (ps == null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    //测试连接
    @Test
    public void testConnection() throws SQLException, IOException, ClassNotFoundException {
        Connection connection = JDBCUtils.getConnection();
        System.out.println(connection);
        //测试成功
    }
    //测试更新
    @Test
    public void testUpdate() throws SQLException, IOException, ClassNotFoundException {
        //1.获取连接对象
        Connection connection = getConnection();
        //2.获得prepareStatement对象
        String sql = "update customers set name = ? where id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setObject(1,"超越妹妹");
        ps.setObject(2,19);
        //3.执行sql语句
        ps.execute();
        //4.关闭连接
        JDBCUtils.closeResource(connection,ps);
        //测试成功
    }

    /**
     * 增删改的统一方法
     * @param sql sql语句
     * @param args 参数
     */
    public static void update(String sql,Object...args){
        //1.创建连接对象 和 PrepareStatement对象
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            //2.获取数据库连接
            conn = JDBCUtils.getConnection();
            //3.获取PrepareStatement对象，进行预编译
            ps = conn.prepareStatement(sql);
            //4.获取参数
            for (int i = 0;i<args.length;i++){
                //这里要注意下标
                //index是从1开始（i+1）,而args数组是从0开始(i)
                ps.setObject(i+1,args[i]);
            }
            //5.执行sql语句
            ps.execute();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //6.关闭数据库连接
            JDBCUtils.closeResource(conn,ps);
        }
    }
    //测试统一的update是否可以
    @Test
    public void testCommonUpdate() throws ParseException {
//        //创建sql语句
//        String sql = "update customers set name = ? where id = ?";
//        //测试
//        JDBCUtils.update(sql,"莫扎特",18);
//        //成功
//        String sql = "delete from `order` where order_id = ?";
//        JDBCUtils.update(sql,2);
//        //成功,要注意像order这样的表名占用了保留字，需要加 ` ` （在tab上面）
        String sql = "insert into customers(name,email,birth) values(?,?,?)";
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        java.util.Date date = sdf.parse("1997-9-15");
        java.util.Date date = DateUtils.getDate("yyyy-MM-dd","1996-3-23");
        JDBCUtils.update(sql,"喵喵","bingbing@xs.com",new Date(date.getTime()));
        //成功

    }
}
