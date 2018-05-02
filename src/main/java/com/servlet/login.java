package com.servlet;

import com.talk51.Utils.JDBCConnectionPool;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet implementation class login
 */
@WebServlet(name = "login", value = "/login")
public class login extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        HttpSession session = request.getSession();
        if (loginCheck(username, password) != null) {
            session.setAttribute("userid", loginCheck("test", "123"));
            session.setAttribute("username", loginCheck("test", "123"));
            response.sendRedirect("Centre.jsp");
        }
    }

    private List loginCheck(String UserName, String Password) {
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
