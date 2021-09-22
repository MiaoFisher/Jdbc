package com.jdbc.QueryRunnerTest;

import com.jdbc.pojo.Customer;
import com.jdbc.utils4.JDBCUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

//测试查询
//使用QueryRunner
public class queryTest {
    private BeanListHandler<Customer> beanListHandler;

    //使用BeanHandler保存结果集
    @Test
    public void testQuery() throws Exception {
        QueryRunner runner = new QueryRunner();
        Connection connection = JDBCUtils.getConnection();
        String sql = "select id,name,email,birth from customers where id = ?";
        BeanHandler<Customer> customer = new BeanHandler<Customer>(Customer.class);
        Customer customer1 = runner.query(connection, sql, customer, 5);
        System.out.println(customer1);
        com.jdbc.utils.JDBCUtils.closeResource(connection);
    }
    //测试用BeanListHandler
    @Test
    public void testQuery2() throws SQLException {
        Connection conn = null;
        QueryRunner queryRunner = new QueryRunner();
        BeanListHandler<Customer> beanListHandler = null;
        conn = JDBCUtils.getConnection();
        String sql = "select id,name,email,birth from customers where id > ?";
        beanListHandler = new BeanListHandler<>(Customer.class);
        List<Customer> customers = queryRunner.query(conn, sql, beanListHandler, 5);
        customers.forEach(System.out::println);
        com.jdbc.utils.JDBCUtils.closeResource(conn);
    }
    //测试用MapListHandler存放结果结合,这种方法可以不传入类的参数查询
    //将字段封装成key 和 value
    @Test
    public void testQuery3(){
        Connection conn = null;
        QueryRunner queryRunner = new QueryRunner();
        MapListHandler mapListHandler = new MapListHandler();
        try {
            conn = JDBCUtils.getConnection();
            String sql = "select id,name,email,birth from customers where id > ?";
            List<Map<String, Object>> list = queryRunner.query(conn, sql, mapListHandler, 12);
            list.forEach(System.out::println);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            com.jdbc.utils.JDBCUtils.closeResource(conn);
        }
    }
    //使用ScalarHandler查询特殊值
    //查询count(*)
    @Test
    public void testQuery4(){
        Connection conn = null;
        ScalarHandler scalarHandler = new ScalarHandler();
        QueryRunner runner = new QueryRunner();
        try {
            conn = JDBCUtils.getConnection();
            String sql = "select count(*) from customers where id > ?";
            Object query = runner.query(conn, sql, scalarHandler, 15);
            System.out.println(query);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            com.jdbc.utils.JDBCUtils.closeResource(conn);
        }
    }
    //通过匿名内部类的方式去重写ResultSetHandler
    @Test
    public void queryTest5(){
        Connection conn = null;
        QueryRunner queryRunner = new QueryRunner();
        try {
            conn = JDBCUtils.getConnection();
            //通过匿名内部类的方式重写ResultSetHandler
            ResultSetHandler<Customer> resultSetHandler = new ResultSetHandler<Customer>() {
                @Override
                public Customer handle(ResultSet rs) throws SQLException {
                    if (rs.next()){
                        int id = rs.getInt("id");
                        String name = rs.getString("name");
                        String email = rs.getString("email");
                        Date birth = rs.getDate("birth");
                        Customer customer = new Customer(id,name,email,birth);
                        return customer;
                    }
                    return null;
                }
            };
            String sql = "select id,name,email,birth from customers where id = ?";
            Customer customer = queryRunner.query(conn, sql, resultSetHandler, 5);
            System.out.println(customer);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResource(conn);
//            System.out.println(conn);
        }
    }
}
