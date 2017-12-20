<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"
         import="java.sql.*"
         import="java.util.*"
         import="java.io.*"
         import="com.talk51.Utils.*"
         import="java.util.HashMap"
         import="java.util.Map"
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Login</title>
    <%
        request.setCharacterEncoding("UTF-8");
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        int userid = 0;

        String userloginname = null;
        boolean Testflag = false;

    %>
</head>
<body>
</body>
</html>