package com.jdbc.dao;

import com.jdbc.pojo.Customer;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public interface CustomerDao {
    void insert(Connection conn, Customer customer) throws Exception;
    void deleteByID(Connection conn,int id)throws Exception;
    void update(Connection conn,Customer customer)throws Exception;
    List<Customer> getAll(Connection conn)throws Exception;
    Long getCount(Connection conn)throws Exception;
    Date getMaxBirth(Connection conn)throws Exception;
}
