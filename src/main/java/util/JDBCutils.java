package util;

import java.sql.*;

public class JDBCutils {
    private Connection con = null;
    private PreparedStatement ps = null;
    //static code block, use once
    static{
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("Driver register is ready!");
    }
    //encapsulate Connection
    public Connection creatConnection(){
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/userinfo?allowPublicKeyRetrieval=true&useSSL=false", "root", "root");
        }catch(SQLException e){
            e.printStackTrace();
            System.out.println("Connection create fail");
        }
        return con;
    }
    //encapsulate PreparedStatement (could prevent sql injection)
    public PreparedStatement createStatement(String sql){
        Connection con = creatConnection();
        try {
            ps = con.prepareStatement(sql);
        }catch(SQLException e){
            e.printStackTrace();
        }
        return ps;
    }

    public PreparedStatement createStatementWithKeys(String sql){
        Connection con = creatConnection();
        try {
            ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        }catch(SQLException e){
            e.printStackTrace();
        }
        return ps;
    }
    //release the resource
    public void close(){
        if(ps!=null){
            try{
                ps.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        if(con!=null){
            try {
                con.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
    }
    //release resultSet and reuse close()
    public void close(ResultSet rs){
        if(rs!=null){
            try{
                rs.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        close();
    }

}
