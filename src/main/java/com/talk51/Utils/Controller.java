package com.talk51.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    @SuppressWarnings("rawtypes")
    public static List logincheck(String UserName, String Password) {
        List<String> result = new ArrayList<String>();
        Connection conn = JDBCConnectionPool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String Sql = "SELECT * FROM user_info WHERE user_name= '" + UserName + "' AND password='" + Password + "'";
        try {
            ps = conn.prepareStatement(Sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                result.add(rs.getString("id"));
                result.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCConnectionPool.close(rs, ps);
        }
        return result;
    }
}
