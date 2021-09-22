package com.jdbc.dao;

import com.jdbc.pojo.Customer;
//在这里可以更换连接类型
import com.jdbc.utils4.JDBCUtils;
import org.junit.Test;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class CustomerDaoImpl extends BaseDao implements CustomerDao {


    @Override
    public void insert(Connection conn, Customer customer) throws Exception {
        String sql = "insert into customers (name,email,birth) values(?,?,?)";
        update(conn,sql,customer.getName(),customer.getEmail(),customer.getBirth());
    }
    //测试添加
    @Test
    public void testInsert() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date = sdf.parse("1998-7-31");
        Customer customer = new Customer();
        customer.setBirth(new Date(date.getTime()));
        customer.setEmail("tangsan@qq.com");
        customer.setName("力宏");
        insert(JDBCUtils.getConnection(),customer);
    }
    @Override
    public void deleteByID(Connection conn, int id) throws Exception {
        String sql = "delete from customers where id = ?";
        update(conn,sql,id);
    }
    @Test
    public void testDelete() throws Exception {
        deleteByID(JDBCUtils.getConnection(),24);
    }
    @Override
    public void update(Connection conn, Customer customer) throws Exception {
        String sql = "update customers set name = ? where id = ? ";
        update(conn,sql,customer.getName(),customer.getId());
    }
    @Test
    public void testUpdate() throws Exception {
        Customer customer = new Customer();
        customer.setName("王冰冰");
        customer.setId(20);
        update(JDBCUtils.getConnection(),customer);
    }
    @Override
    public List<Customer> getAll(Connection conn) throws Exception {
        String sql = "select id,name,email,birth from customers";
        List<Customer> customers = query(conn, Customer.class, sql);
        return customers;
    }
    @Test
    public void testGetAll() throws Exception {
        List<Customer> customers = getAll(JDBCUtils.getConnection());
        customers.forEach(System.out::println);
    }
    @Override
    public Long getCount(Connection conn) throws Exception {
        String sql = "select count(*) from customers";
        Object count = getValue(conn, sql);
        return (Long) count;
    }
    @Test
    public void testGetCount() throws Exception {
        Long count = getCount(JDBCUtils.getConnection());
        System.out.println(count);
    }

    @Override
    public Date getMaxBirth(Connection conn) throws Exception {
        String sql = "select MAX(birth) from customers";
        Object maxBirth = getValue(conn, sql);
        return (Date) maxBirth;
    }
    @Test
    public void testGetMaxBirth() throws Exception {
        Date maxBirth = getMaxBirth(JDBCUtils.getConnection());
        System.out.println(maxBirth);

    }
}
