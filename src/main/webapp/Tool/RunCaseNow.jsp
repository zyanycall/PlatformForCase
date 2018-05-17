<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"
         import="com.talk51.Utils.*"
         import="java.net.URLDecoder"
         import="com.borderTest.BorderCheckUtil"
%>
<%
    request.setCharacterEncoding("UTF-8");
    String url = URLDecoder.decode(request.getParameter("url"), "UTF-8");
    String bodyinfo = request.getParameter("bodyinfo");
//    String DisBody = URLDecoder.decode(bodyinfo, "UTF-8");
    String method = request.getParameter("method");
%>
所请求url和参数：<br>
<%=url%>?<%=bodyinfo.replaceAll("&timestamp", "&amp;timestamp")%><br>
所传参数明细：<br>
<%=bodyinfo.replaceAll("&timestamp", "&amp;timestamp")%><br>
运行结果：<%=method%> <br>
<%--<PRE style="font-size:16px">--%>
<%
    if (method.endsWith("oneKeyCheck")) {
        out.print(HttpMethodFactory.getResult(method, url, bodyinfo));
    } else {
        String resultWithTime = HttpMethodFactory.getResultWithTime(method, url, bodyinfo);
        out.print(resultWithTime.replace(HttpMethodFactory.EXECUTE_TIME_SEPARATOR,""));
        String result = BorderCheckUtil.toJson(HttpMethodFactory.getResultData(resultWithTime)) == null ? "返回为空":
                BorderCheckUtil.toJson(HttpMethodFactory.getResultData(resultWithTime));
        out.print("<br><br>格式化：<br>" + BorderCheckUtil.HTML_POINT_GREEN
                + result + BorderCheckUtil.HTML_RESULT_SUFFIX);
    }
%>
<%--</PRE>--%>