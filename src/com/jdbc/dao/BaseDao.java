package com.jdbc.dao;

import com.jdbc.pojo.Customer;
import com.jdbc.utils.JDBCUtils;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseDao {
    //更新操作
    public static void update(Connection conn, String sql, Object... args) {
        PreparedStatement ps = null;
        try {
            conn = JDBCUtils.getConnection();
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(ps);
        }
    }

    //查询通用操作
    public static <T> List<T> query(Connection conn, Class<T> clazz, String sql, Object... args) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection();
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            List<T> list = new ArrayList<T>();
            while (rs.next()) {
                //通过泛型创建新的对象
                T t = clazz.newInstance();
                for (int i = 0; i < columnCount; i++) {
                    Object columnValue = rs.getObject(i + 1);
                    String columnLabel = rsmd.getColumnLabel(i + 1);
                    //通过反射配置属性
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t,columnValue);
                }
                list.add(t);
            }return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource( ps, rs);
        }
        return null;
    }
    //返回特殊值
    public static <E>E getValue(Connection conn,String sql,Object...args){
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection();
            ps = conn.prepareStatement(sql);
            for (int i = 0;i<args.length;i++){
                ps.setObject(i+1,args[i]);
            }
            rs = ps.executeQuery();
            if (rs.next()){
                return (E) rs.getObject(1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResource(ps,rs);
        }
        return null;
    }
    //此类为抽象类,下面的测试是在没有改为抽象类之前测试的，均成功
//    //测试update
//    @Test
//    public void testUpdate() throws SQLException, IOException, ClassNotFoundException {
//        Connection conn = JDBCUtils.getConnection();
//        String sql = "insert into user_table (user,password,balance)values(?,?,?)";
//        update(conn,sql,"EE","985211",1500);
//        conn.close();
//    }
//    //测试查询
//    @Test
//    public void testQuery() throws SQLException, IOException, ClassNotFoundException {
//        Connection conn = JDBCUtils.getConnection();
//        String sql = "select id,name,email,birth from customers where id < ?";
//        List<Customer> customers = query(conn, Customer.class, sql, 12);
//        customers.forEach(System.out::println);
//        conn.close();
//    }
//    //测试查询单值
//    @Test
//    public void testGetValue() throws SQLException, IOException, ClassNotFoundException {
//        Connection conn = JDBCUtils.getConnection();
//        String sql = "select count(*) from customers where id > ?";
//        Object value = getValue(conn, sql, 5);
//        System.out.println(value);
//
//    }
}
