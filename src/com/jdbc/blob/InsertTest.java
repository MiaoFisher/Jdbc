package com.jdbc.blob;

import com.jdbc.utils.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class InsertTest {
    //测试插入大量数据
    @Test
    public void testInsert(){
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = JDBCUtils.getConnection();
            //设置数据库连接不允许自动提交
            conn.setAutoCommit(false);
            String sql = "insert into goods(name) values (?)";
            ps = conn.prepareStatement(sql);
            long begin = System.currentTimeMillis();
            for (int i = 0;i<10000;i++){
                ps.setObject(1,"name_"+(i+1));
                ps.execute();
                //将sql语句先不执行
                //将sql命令积累到batch池中
                ps.addBatch();
                //当积累的sql达到500的时候才开始一起执行
                if (i % 5 == 500){
                    ps.executeBatch();
                    //同时清空Batch
                    ps.clearBatch();
                }
            }
            conn.commit();
            long end = System.currentTimeMillis();
            System.out.println(end - begin);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResource(conn,ps);
        }
    }
}
