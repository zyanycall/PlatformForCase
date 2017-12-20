<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="com.talk51.Utils.*"
    import="java.net.URLDecoder"
%>
<%
request.setCharacterEncoding("UTF-8");
String url=URLDecoder.decode(request.getParameter("url"),"UTF-8");
String bodyinfo=request.getParameter("bodyinfo");
String DisBody=URLDecoder.decode(bodyinfo,"UTF-8");
String method=request.getParameter("method");
%>
所请求url和参数：<br>
<%=url%>?<%=DisBody.replaceAll("&timestamp","&amp;timestamp")%><br>
所传参数明细：<br>
<%=DisBody.replaceAll("&timestamp","&amp;timestamp")%><br>
运行结果：<%=method%> <br>
<%=HttpMethodFactory.getResult(method, url, bodyinfo)%>