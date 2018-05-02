<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>主页面</title>
</head>
<body onkeydown="enterClick(event.keyCode)">
<script>
    function enterClick(keyCode) {
        if (keyCode == 13) {
            var btn=document.getElementById('defaultHref');
            btn.click()
        }
    }
</script>

欢迎您，名叫<%=session.getAttribute("username")%>的QA或者RD,您的用户ID为<%=session.getAttribute("userid")%>
您可以进行如下操作：<br>
<a href="AddCase/Step1.jsp">创建新测试用例</a><br>
<a id='defaultHref' href="UpdataCase/UpdataStep1.jsp">管理现有测试用例</a><br>
</body>
</html>