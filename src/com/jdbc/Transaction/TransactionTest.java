package com.jdbc.Transaction;

import com.jdbc.utils.JDBCUtils;
import org.junit.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TransactionTest {
    /**
     * 需要自行传入Connection对象，以便于控制事务
     * @param conn
     * @param sql
     * @param args
     * @throws SQLException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void update(Connection conn,String sql,Object...args) throws SQLException, IOException, ClassNotFoundException {

        PreparedStatement ps = conn.prepareStatement(sql);
        for (int i = 0;i<args.length;i++){
            ps.setObject(i+1,args[i]);
        }
        ps.execute();
        JDBCUtils.closeResource(null,ps);
    }
    @Test
    public void testTxUpdate(){
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            //取消自动提交
            conn.setAutoCommit(false);
            String sql1 = "update user_table set balance = balance - 100 where user = ?";
            String sql2 = "update user_table set balance = balance + 100 where user = ?";
            update(conn,sql1,"AA");
            int i = 10/0;
            update(conn,sql2,"BB");
            //事务提交
            conn.commit();
            System.out.println("事务提交成功");
        }catch (Exception e){
            e.printStackTrace();
            //抛出异常的时候回滚
            try {
                conn.rollback();
                System.out.println("事务提交异常，已回滚数据");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }finally {
            //在关闭之前要恢复自动提交
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            //最后还是要关闭连接
            JDBCUtils.closeResource(conn,null);
        }

    }
}
