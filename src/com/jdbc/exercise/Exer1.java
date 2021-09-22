package com.jdbc.exercise;

import com.jdbc.utils.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Exer1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("name = ");
        String name = scanner.next();
        System.out.println("email = ");
        String email = scanner.next();
        System.out.println("birth = ");
        String birth = scanner.next();
        String sql = "insert into customers (name,email,birth) values(?,?,?)";
        int count = update(sql, name, email, birth);
        if (count>0){
            System.out.println("插入成功");
        }else {
            System.out.println("插入失败");
        }
    }
    public static int update(String sql,Object...args){
        Connection conn = null;
        PreparedStatement ps = null;
        try{
            conn = JDBCUtils.getConnection();
            ps = conn.prepareStatement(sql);
            for (int i = 0;i<args.length;i++){
                ps.setObject(i+1,args[i]);
            }
            //执行sql语句并且返回影响行数
            int i = ps.executeUpdate();
            return i;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResource(conn,ps);
        }
        return 0;
    }
}
