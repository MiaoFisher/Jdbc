package com.jdbc.dao2;

import com.jdbc.pojo.Customer;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

public interface CustomerDao {
    //将update传入的Class参数删掉
    void insert(Connection conn,Customer customer) throws Exception;
    void deleteByID(Connection conn, int id)throws Exception;
    void update(Connection conn,Customer customer)throws Exception;
    List<Customer> getAll(Connection conn)throws Exception;
    Long getCount(Connection conn)throws Exception;
    Date getMaxBirth(Connection conn)throws Exception;
}
