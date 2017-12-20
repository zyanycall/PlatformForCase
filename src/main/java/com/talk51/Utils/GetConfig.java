package com.talk51.Utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class GetConfig {

    static Properties Config = new Properties();

    @SuppressWarnings("null")
    public static Map<String, String> getConfig() {
        Map<String, String> result = new HashMap<String, String>();

        try {

            URL classpath = Thread.currentThread().getContextClassLoader().getResource("");
            InputStream Tempin = new BufferedInputStream(new FileInputStream(classpath.getFile() + "JDBCConfig.properties"));

            Config.load(Tempin);
            result.put("ip", Config.getProperty("Test.db.ip"));
            result.put("port", Config.getProperty("Test.db.port"));
            result.put("user", Config.getProperty("Test.db.user"));
            result.put("dbname", Config.getProperty("Test.db.dbname"));
            result.put("driver", Config.getProperty("Test.db.drive"));
            result.put("password", Config.getProperty("Test.db.password"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String getip() {
        String ip = getConfig().get("ip");
        return ip;
    }

    public static String getport() {
        String port = getConfig().get("port");
        return port;
    }

    public static String getuser() {
        String user = getConfig().get("user");
        return user;
    }

    public static String getdbname() {
        String dbname = getConfig().get("dbname");
        return dbname;
    }

    public static String getdriver() {
        String driver = getConfig().get("driver");
        return driver;
    }

    public static String getpassword() {
        String password = getConfig().get("password");
        return password;
    }
}
