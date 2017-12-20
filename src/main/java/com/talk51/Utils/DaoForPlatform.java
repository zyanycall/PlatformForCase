package com.talk51.Utils;

import java.sql.*;

public class DaoForPlatform {

    public static Connection getConnection() {
        String url = "" + GetConfig.getdriver() + "://" + GetConfig.getip() + ":" + GetConfig.getport() + "/" + GetConfig.getdbname() + "?user=" + GetConfig.getuser() + "&password=" + GetConfig.getpassword();
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(url);
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return conn;
    }

    public static void closeAll(ResultSet rs, Statement st, Connection conn) {  //关闭连接（用于增删改）
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
