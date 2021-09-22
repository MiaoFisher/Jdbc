package com.jdbc.PrepareStatement;

import com.jdbc.pojo.Customer;
import com.jdbc.utils.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;

//这里用customer表测试查询操作
public class CustomerForQuery {
    @Test
    public void testQuery() {
        //1.创建连接对象
        Connection conn = null;
        //2.创建PrepareStatement对象
        PreparedStatement ps = null;
        //3.创建ResultSet对象
        ResultSet rs = null;
        try {
            //获取连接对象Connection
            conn = JDBCUtils.getConnection();
            //获取prepareStatement对象
            String sql = "select id,name,email,birth from customers where id = ?";
            ps = conn.prepareStatement(sql);
            ps.setObject(1, 1);
            //执行sql语句并返回结果集
            rs = ps.executeQuery();
            //根据结果集获取每个属性值
            if (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                String email = rs.getString(3);
                Date date = rs.getDate(4);
                Customer customer = new Customer(id, name, email, date);
                System.out.println(customer.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭数据库连接
            JDBCUtils.closeResource(conn, ps, rs);
        }
    }

    /**
     * 针对customer表的通用操作
     *  @param sql
     * @param args
     * @return 返回一个Customer对象
     */
    public static Customer queryForCustomer(String sql, Object... args) {
        //创建连接对象，结果集对象，(预)操作对象
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtils.getConnection();
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            //获取结果集
            rs = ps.executeQuery();
            //获取结果集的元数据
            ResultSetMetaData rsmd = rs.getMetaData();
            //通过元数据集ResultSetMetaData获取列数
            int columnCount = rsmd.getColumnCount();
            //rs.next()才是指向第一条数据
            if (rs.next()) {
                Customer customer = new Customer();
                for (int i = 0; i < columnCount; i++) {
                    //获取列值
                    Object columnValue = rs.getObject(i+1);
                    //获取列名
                    String columnName = rsmd.getColumnName(i+1);
                    //通过反射给指定字段添加值
                    Field field = Customer.class.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(customer,columnValue);
                }
                return customer;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭数据库连接（关闭各项资源）
            JDBCUtils.closeResource(conn, ps, rs);
        }
        return null;
    }
    @Test
    public void testCustomerQuery(){
        String sql = "select id,name,email,birth from customers where id = ?";
        Customer customer = queryForCustomer(sql, 1);
        System.out.println(customer);
    }
}
