package test;

import org.junit.Test;
import util.JDBCutils;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class JDBCTest {

    @Test
    public void testQuery() {

        JDBCutils util = new JDBCutils();
        ResultSet resultSet = null;
        try {
            String sql = "select * from userpost";
            PreparedStatement ps = util.createStatement(sql);
            resultSet = ps.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString("user");
                int id = resultSet.getInt("id");
                System.out.println("user=" + name + ",ID=" + id);
            }
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            util.close();
        }

    }

    @Test
    public void testInsert() {
        JDBCutils util = new JDBCutils();
        try {
            String sql = "insert into userpost (post,user,date,title) values('aa','AAA','2020-11-15 21:22:23 UTC','Test')";
            PreparedStatement ps = util.createStatement(sql);
            int rs = ps.executeUpdate();
            if (rs > 0) {
                System.out.println("Insert Success");
            } else {
                System.out.println("Insert Fail");
            }
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            util.close();
        }
    }
}

