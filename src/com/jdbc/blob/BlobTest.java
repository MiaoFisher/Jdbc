package com.jdbc.blob;

import com.jdbc.utils.JDBCUtils;
import org.junit.Test;

import java.io.*;
import java.sql.*;

public class BlobTest {
    //测试如何插入图片
    @Test
    public void testInsert() throws SQLException, IOException, ClassNotFoundException {
        Connection conn = JDBCUtils.getConnection();
        String sql = "insert into customers (name,email,birth,photo) values(?,?,?,?) ";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setObject(1,"张楠");
        ps.setObject(2,"zhangnan@qq.com");
        ps.setObject(3,"1950-5-8");
        //以流的形式去得到图片
        FileInputStream fis = new FileInputStream(new File("2.jpg"));
        ps.setBlob(4,fis);
        ps.execute();
        if (fis == null) {
            fis.close();
        }
    }
    //如何读取图片
    @Test
    public void testGet(){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "select id,name,email,birth,photo from customers where id = ?";
            ps = conn.prepareStatement(sql);
            ps.setObject(1,23);
            rs = ps.executeQuery();
            //int id = rs.getInt("id");
            rs.next();
            String name = rs.getString("name");
            String email = rs.getString("email");
            Date bitrh = rs.getDate("birth");
            Blob photo = rs.getBlob("photo");
            //通过流获取照片
            //获得照片转换为二进制流
            is = photo.getBinaryStream();
            fos = new FileOutputStream("ycc.jpg");
            byte[] buffer = new byte[1024];
            int len;
            while ((len = is.read(buffer))!=-1){
                fos.write(buffer,0,len);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (fos == null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is == null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            JDBCUtils.closeResource(conn,ps,rs);
        }
    }
}
