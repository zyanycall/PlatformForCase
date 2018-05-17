<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="java.util.*"
    import="java.io.*"
    %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="../js/jquery-3.1.0.min.js"></script>
<title>结果显示页</title>
</head>
<body>
<%
request.setCharacterEncoding("UTF-8");
String result=request.getParameter("result");
%>
<%=result.replaceAll("&timestamp", "&amp;timestamp")%>
</body>