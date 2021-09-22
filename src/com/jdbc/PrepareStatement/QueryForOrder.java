package com.jdbc.PrepareStatement;

import com.jdbc.pojo.Order;
import com.jdbc.utils.JDBCUtils;
import com.sun.org.apache.xpath.internal.operations.Or;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

//针对于Order表的操作
public class QueryForOrder {
    public Order queryForOrder(String sql,Object...args){
        //创建连接对象
        Connection conn = null;
        //创建操作对象
        PreparedStatement ps = null;
        //创建结果集对象
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection();
            ps = conn.prepareStatement(sql);
            //将参数传入
            for (int i = 0;i<args.length;i++){
                ps.setObject(i+1,args[i]);
            }
            //得到结果集
            rs = ps.executeQuery();
            //得到结果集的元数据
            ResultSetMetaData rsmd = rs.getMetaData();
            //得到数据集的行数
            int coulumCount = rsmd.getColumnCount();
            //取出数据
            if (rs.next()){
                Order order = new Order();
                for (int i = 0;i<coulumCount;i++){
                    //获得列值
                    Object columnValue = rs.getObject(i+1);
                    //获得列名 getColumnLabel可以获得表的Label,也就是可以获得别名，从而解决pojo类和数据库中的属性值名字不同的情况
                    //推荐使用这种方法
                    String columnLabel = rsmd.getColumnLabel(i+1);
                    //通过反射设置将设置属性
                    Field field = Order.class.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(order,columnValue);
                }
                return order;
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //关闭数据库连接，关闭资源
            JDBCUtils.closeResource(conn,ps,rs);
        }
        return null;
    }
    //测试order表的通用操作
    @Test
    public void testQueryForOrder(){
        //这里需要注意三点
        //1.在sql语句中要增加别名，让其能和javabean中的列匹配
        //2.order是保留字，所以需要添加``(在tab上)
        //3.在最后的查询还是要用原来的字段 如order_id,不能用id
        String sql = "select order_id id,order_name name,order_date date from `order` where order_id = ?";
        Order order = queryForOrder(sql, 1);
        System.out.println(order);
    }
}
