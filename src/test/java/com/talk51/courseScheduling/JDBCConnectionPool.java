package com.talk51.courseScheduling;

import java.sql.*;

/**
 * Created by zhaoyu on 2017/3/22.
 */
public class JDBCConnectionPool {

    //同步没有风险，一个对象针对不同线程集合
    private static ThreadLocal<Connection> threadLocal = new ThreadLocal<Connection>();
    //数据库url
    private String url = "";

    JDBCConnectionPool(String url) {
        super();
        this.url = url;
    }

    public Connection getConnection() {
        Connection connection = threadLocal.get();
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                connection = DriverManager.getConnection(url);
                threadLocal.set(connection);
                return connection;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return connection;
    }

    public void close(ResultSet rs, Statement stmt) {
        try {
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeConnection();
    }

    public void close(Statement stmt) {
        try {
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeConnection();
    }

    public static void closeConnection() {
        Connection connection = threadLocal.get();
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                threadLocal.set(null);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // 回滚应该用不到
    public void connectionRollback(){
        Connection connection = threadLocal.get();
        try {
            if (connection != null && !connection.isClosed()) {
                connection.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
