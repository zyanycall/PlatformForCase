<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"
         import="java.sql.*"
         import="com.talk51.Utils.JDBCConnectionPool"
         import="javax.servlet.http.HttpSession"
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

        Connection connection = JDBCConnectionPool.getConnection();
        Statement statement = connection.createStatement();
        String sql = "SELECT * FROM user_info WHERE user_name= '" + username + "' AND password='" + password + "'";
        ResultSet rs = statement.executeQuery(sql);
        if (rs.next()) {
            Testflag = true;
            userid = Integer.parseInt(rs.getString("id"));
            userloginname = rs.getString("user_name");
        } else {
            Testflag = false;
        }
        rs.close();
        statement.close();
        JDBCConnectionPool.closeConnection();
    %>
</head>
<body>
<%
    if (!Testflag) {
%>
<script language=javascript>
    alert("登陆失败，密码错误或用户名不存在")
    window.location.href = "index.html";
</script>
<%
} else {
%>
<script language=javascript>
    <%
    session.setAttribute("userid",userid);
    session.setAttribute("username",userloginname);
    %>
    window.location.href = "Centre.jsp";
</script>
<%
    }
%>
</body>
</html>