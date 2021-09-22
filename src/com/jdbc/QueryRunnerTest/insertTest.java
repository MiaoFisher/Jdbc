package com.jdbc.QueryRunnerTest;

import com.jdbc.utils4.JDBCUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

public class insertTest {
    //测试插入
    @Test
    public void testInsert() throws SQLException {
        QueryRunner queryRunner = new QueryRunner();
        Connection connection = JDBCUtils.getConnection();
        String sql = "insert into customers(name,email,birth) values (?,?,?)";
        int i = queryRunner.update(connection,sql, "酸菜鱼", "suancaiyu@m.com", "2001-5-15");
        System.out.println(i);
        com.jdbc.utils.JDBCUtils.closeResource(connection);
        //test pass
    }
}
