package com.talk51.Utils;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Created by zhaoyu on 2017/3/22.
 */
public class JDBCConnectionPool {

    //同步没有风险
    private static ThreadLocal<Connection> threadLocal = new ThreadLocal<Connection>();

    public static Connection getConnection() {
        Connection connection = threadLocal.get();
        try {
            if (connection == null || connection.isClosed()) {
                connection = DruidConnectionManager.getInstance().getConnection();
                threadLocal.set(connection);
                return connection;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void close(ResultSet rs, Statement stmt) {
        DruidConnectionManager.getInstance().closeAllResources(rs, stmt);
        closeConnection();
    }

    public static void close(Statement stmt){
        DruidConnectionManager.getInstance().closeStatement(stmt);
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

    public static void connectionRollback(){
        Connection connection = threadLocal.get();
        try {
            if (connection != null && !connection.isClosed()) {
                connection.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws IOException {
        URL classpath = Thread.currentThread().getContextClassLoader().getResource("");
        InputStream Tempin = new BufferedInputStream(new FileInputStream(classpath.getFile() + "JDBCConfig.properties"));
        Properties Config = new Properties();
        Config.load(Tempin);
        String dbip = Config.getProperty("Test.db.ip");
        System.out.println(dbip);
    }
}

/**
 * 连接池类只有JDBC使用，故写在一个文件中。
 * Druid被誉为最快的连接池，这里尝试一下。
 */
class DruidConnectionManager {

    private static DruidConnectionManager druidConnection;
    private static DataSource datasource;
    private static Properties p = new Properties();

    //资源文件写成单例模式
    private DruidConnectionManager() {
        datasource = initDataSource();
    }

    private static DataSource initDataSource() {
        try {
            p.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("JDBCConfig.properties"));
            datasource = DruidDataSourceFactory.createDataSource(p);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return datasource;
    }

    public static synchronized DruidConnectionManager getInstance() {

        if (druidConnection == null) {
            druidConnection = new DruidConnectionManager();
        }
        return druidConnection;
    }

    public Connection getConnection() {
        if (datasource != null) {
            try {
                Connection connection = datasource.getConnection();
                return connection;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void closeAllResources(ResultSet rs, Statement stmt) {
        closeResultSet(rs);
        closeStatement(stmt);
    }

    public void closeResultSet(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeStatement(Statement stmt) {
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}