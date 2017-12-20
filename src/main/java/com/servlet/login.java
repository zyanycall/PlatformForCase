package com.servlet;

import com.talk51.Utils.Controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet implementation class login
 */
@WebServlet(name = "login", value = "/login")
public class login extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        HttpSession session = request.getSession();
        if (Controller.logincheck(username, password) != null) {
            session.setAttribute("userid", Controller.logincheck("test", "123"));
            session.setAttribute("username", Controller.logincheck("test", "123"));
            response.sendRedirect("Centre.jsp");
        }
    }

}
