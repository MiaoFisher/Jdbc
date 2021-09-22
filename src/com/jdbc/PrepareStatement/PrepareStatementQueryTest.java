package com.jdbc.PrepareStatement;

import com.jdbc.pojo.Customer;
import com.jdbc.pojo.Order;
import com.jdbc.utils.JDBCUtils;
import com.sun.org.apache.xpath.internal.operations.Or;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//通用表测试
public class PrepareStatementQueryTest {
    /**
     *针对于不同表的查询操作
     * @param clazz 类的类型
     * @param sql sql语句
     * @param args 参数
     * @param <T>
     * @return 返回一个类的实例（？）
     */
    public <T>T getInstance(Class<T> clazz,String sql,Object...args){
        //创建连接对象
        Connection conn = null;
        //创建操作对象
        PreparedStatement ps = null;
        //创建结果集对象
        ResultSet rs = null;
        try {
            //获得数据库连接
            conn = JDBCUtils.getConnection();
            //执行预处理
            ps = conn.prepareStatement(sql);
            //传入参数
            for (int i = 0;i<args.length;i++){
                ps.setObject(i+1,args[i]);
            }
            //获得结果集
            rs = ps.executeQuery();
            //获得结果集的元数据
            ResultSetMetaData rsmd = rs.getMetaData();
            //根据结果集元数据获得数据的列数
            int columnCount = rsmd.getColumnCount();
            //将数据取出
            if (rs.next()){
                //通过泛型创建对象
                T t = clazz.newInstance();
                for (int i = 0;i<columnCount;i++){
                    //取出列值
                    Object columnValue = rs.getObject(i + 1);
                    //获得列名
                    String columnLabel = rsmd.getColumnLabel(i + 1);
                    //通过反射去获得属性
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t,columnValue);
                }
                return t;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //关闭数据库连接，各项资源连接
            JDBCUtils.closeResource(conn,ps,rs);
        }
        return null;
    }
    //测试通用表查询
    @Test
    public void testGetInstance(){
        String sql = "select id,name,email,birth from customers where id = ?";
        Customer customer = getInstance(Customer.class, sql, 1);
        System.out.println(customer);

        sql = "select order_id id,order_name name from `order` where order_id = ?";
        Order order = getInstance(Order.class, sql, 4);
        System.out.println(order);
    }

    /**
     * 查询多条记录
     * @param <T>
     * @return
     */
    public <T>List<T> getForList(Class<T> clazz, String sql,Object...args){
        //创建连接对象
        Connection conn = null;
        //创建操作对象
        PreparedStatement ps = null;
        //创建结果集对象
        ResultSet rs = null;
        try {
            //获得数据库连接
            conn = JDBCUtils.getConnection();
            //执行预处理
            ps = conn.prepareStatement(sql);
            //传入参数
            for (int i = 0;i<args.length;i++){
                ps.setObject(i+1,args[i]);
            }
            //获得结果集
            rs = ps.executeQuery();
            //获得结果集的元数据
            ResultSetMetaData rsmd = rs.getMetaData();
            //根据结果集元数据获得数据的列数
            int columnCount = rsmd.getColumnCount();
            //将数据取出
            //创建列表用于返回
            List<T> list = new ArrayList<T>();
            while (rs.next()){
                //通过泛型创建对象
                T t = clazz.newInstance();
                for (int i = 0;i<columnCount;i++){
                    //取出列值
                    Object columnValue = rs.getObject(i + 1);
                    //获得列名
                    String columnLabel = rsmd.getColumnLabel(i + 1);
                    //通过反射去获得属性
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t,columnValue);
                }
                list.add(t);
            }
            return list;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //关闭数据库连接，各项资源连接
            JDBCUtils.closeResource(conn,ps,rs);
        }
        return null;
    }
    @Test
    public void testGetForList(){
        String sql = "select id,name,email,birth from customers where id < ?";
        List<Customer> list = getForList(Customer.class, sql, 12);
        list.forEach(System.out::println);
    }

}
