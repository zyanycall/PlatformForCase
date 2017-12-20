<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="java.sql.*"
    import="java.util.*"
    import="java.io.*"
    import="com.talk51.Utils.JDBCConnectionPool"
    %>
<%

String modelname=request.getParameter("modelname");

Connection connection=JDBCConnectionPool.getConnection();
Statement statement = connection.createStatement();
String sql="SELECT distinct * FROM interface_list where model_name='"+modelname+"'";
ResultSet rs = statement.executeQuery (sql);
while(rs.next())
{
%>
<%=rs.getString("interface_name")%>:
<%=rs.getString("call_Stuff")%>:<%
}
rs.close(); 
statement.close(); 
JDBCConnectionPool.closeConnection();
%>