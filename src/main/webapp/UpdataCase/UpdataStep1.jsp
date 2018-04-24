<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"

         import="java.sql.*"
         import="com.talk51.Utils.JDBCConnectionPool"

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <link rel="stylesheet" type="text/css" href="../css/updatastep1.css">
    <script src="../js/jquery-3.1.0.min.js"></script>
    <meta http-equiv="Content-Type" content="text/html; UTF-8">
    <title>修改测试用例</title>
    <%
        request.setCharacterEncoding("UTF-8");
        int user_id = 0;
        if (session.getAttribute("userid") == null) {
    %>
    <script language=javascript>
        alert("您尚未登陆或者登陆信息已失效，请重新登陆")
    </script>
    <%
            response.sendRedirect("../index.html");
            return;
        } else {
            Object O = session.getAttribute("userid");
            user_id = Integer.parseInt(O == null ? "" : O.toString());
        }

    %>
</head>
<body onkeydown="enterClick(event.keyCode)">
<div>
    请选择测试用例所属模块<br>
    选择模块：<br><SELECT name="ModelSelect" id="model_name">
    <OPTION value="default" selected>请选择...</OPTION>
    <%
        Connection connection = JDBCConnectionPool.getConnection();
        Statement statement = connection.createStatement();
        String sql = "SELECT distinct model_name FROM interface_list";
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
    %>
    <OPTION value="<%=rs.getString("model_name")%>"><%=rs.getString("model_name")%>
    </OPTION>
    <%
        }
        JDBCConnectionPool.close(rs, statement);
    %>
</SELECT><br>
</div>
<div>
    优先级
    <SELECT name="prority_select" id="prority">
        <OPTION value="1,2,3,10" selected>全部</OPTION>
        <OPTION value="1">1</OPTION>
        <OPTION value="2">2</OPTION>
        <OPTION value="3">3</OPTION>
        <OPTION value="10">10</OPTION>
    </SELECT>
    call_suff like：<input type="text" id="call_suff" name="call_suff" style="width:500px;"/>
    <input id="refresh_search" type="button" value="搜索" onclick="refresh()">
</div>
<div id="case_list">
    请选择模块
</div>
<div>
    <input type="button" value="批量运行所选择用例" onclick="getcaseids()">
    <input type="button" value="批量删除所选择用例" onclick="deleteselected()">
    <input type="button" value="全选" onclick="selectall()">
    <input type="button" value="取消全选" onclick="unselectall()">
    <input type="button" value="批量运行输入用例id" onclick="testInput()">
    <input type="button" value="刷新" onclick="refresh()">
</div>
<div>
    输入用例ids，逗号隔开，可以跨模块 ：
    <input name="ids_input" type="text" id="ids_input" value="" size="85"/>
    <br>
    如果需要在用例保存之外的环境运行用例，请选择<br>
    已有环境：<SELECT name="ip" id="ip_another">
    <OPTION value="none" selected>&lt;使用用例本身IP地址&gt;</OPTION>
    <%
        Connection connection2 = JDBCConnectionPool.getConnection();
        Statement statement2 = connection2.createStatement();
        String sql2 = "SELECT * FROM interface_ip";
        ResultSet rs2 = statement2.executeQuery(sql2);
        while (rs2.next()) {
    %>
    <OPTION value="<%=rs2.getString("ip")%>:<%=rs2.getString("port")%>"><%=rs2.getString("ip")%>
        :<%=rs2.getString("port")%>
    </OPTION>
    <%
        }
        JDBCConnectionPool.close(rs2, statement2);
    %>
</SELECT>
    或者手动输入IP ： <input name="ip_input" type="text" id="ip_input" value=""/>
    <br>
    <br>
</div>
<form id="try" action="../Tool/Result.jsp" method="post" target="_blank">
    <input id="result_try" name="result" type="hidden"/>
</form>

<br><br>
<------------------下面主要是为了简单格式的一键边界测试使用--------------------->
<br><br>
<div id="executePartMethod" style="border:1px solid #000;padding:4px">
    请选择目标接口的请求方法<br>
    <input name="method" type="radio" id="method_get" value="get" checked/>Get
    <input name="method" type="radio" id="method_post" value="post"/>Post
    <input name="method" type="radio" id="method_post_body" value="post_Json"/>Post_Body
    <input name="method" type="radio" id="post_return_json" value="post_return_json"/>Post_Return_Json
    <input name="method" type="radio" id="put" value="put"/>Put
    <input name="method" type="radio" id="delete" value="delete"/>Delete
</div>
<div id="executePartUrl" style="border:1px solid #000;padding:4px">
    URL：<input type="text" id="executeUrl" name="interface_suff" style="width:1000px;"/>
    <br>
</div>
<div id="executePartBodyInfo" style="border:1px solid #000;padding:4px">
    请求内容（如果是get请求可以直接将请求黏贴到这里）<br>
    <textarea name="executeBodyInfo" id="executeBodyInfo" style="width:1050px;height:80px"/></textarea><br>
</div>
<div id="executePartButton" style="border:1px solid #000;padding:4px">
    <input type="button" value="执行" onclick="executeTest()"/>
    <input type="button" value="一键边界测试" onclick="executeTest(100)"/>
</div>

</body>
<script language=javascript>
    function enterClick(keyCode) {
        if (keyCode == 13) {
            var btn=document.getElementById('refresh_search');
            btn.click()
        }
    }

    function executeTest(flag) {
        var xmlhttp;
        if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
            xmlhttp = new XMLHttpRequest();
        }
        else {// code for IE6, IE5
            xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
        }
        xmlhttp.onreadystatechange = function () {
            if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                var save = xmlhttp.responseText;
                sendresult(save);
            }
        }

        var method = $("input[name='method']:checked").val();
        var url = $("#executeUrl").val();
        var bodyInfo = $("#executeBodyInfo").val();

        // 空请求直接返回
        if (!url && !bodyInfo) {
            return;
        }

        if (!url && method == 'get' && bodyInfo) {
            var strs = bodyInfo.split('?')
            url = strs[0];
            bodyInfo = strs[1];

        }
        if (flag == 100) {
            method = method + "@oneKeyCheck";

        }

        xmlhttp.open("POST", "../Tool/RunCaseNow.jsp", true);
        xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        var executeContent = "url=" + url + "&bodyinfo=" + encodeURIComponent(bodyInfo) + "&method=" + method;
        xmlhttp.send(executeContent);
    }

    function oneKeyCheck() {

    }

    function sendresult(result) {
        document.getElementById("result_try").value = result;
        document.getElementById("try").submit();
    }

    var userid = <%=user_id%>
        $("#model_name").change(
            function () {
                refresh();
            })

    $("#prority").change(
        function () {
            refresh();
        })

    function deleteselected() {
        var ids = "";
        var r = confirm("是否真的删除？");
        if (r == true) {
            $('input[name="select"]:checked').each(function () {
                if (ids == "") {
                    ids = $(this).val();
                } else {
                    ids = ids + "," + $(this).val();
                }
            });
        } else {
            return false;
        }
        if (ids == "") {
            alert("您没有选择任何用例");
            return false
        }
        $.post(
            "../batchDelCases",
            {preids: ids},
            function (data) {
                if (data == "error") {
                    alert("删除失败！");
                } else {
                    alert("删除成功！");
                    location.reload();
                }
            })
    }


    function getcaseids() {
        var chk_value = [];
        var result = "";
        var anotherip = $("#ip_another option:selected").val();
        $('input[name="select"]:checked').each(function () {
            result = result + ($(this).val()) + ",";
        });

        if (result == "") {
            alert("您没有选择任何用例");
            return false
        }

        if (document.getElementById("ip_input").value != "") {
            anotherip = document.getElementById("ip_input").value;
        }
        $.post("../runmuilttest_new",
            {preids: result, another_ip: anotherip},
            function (data) {

                sendresult(data);
            }
        )
    }

    function refresh() {

        debugger;

        var getinfo = $("#model_name option:selected").val();
        var getprority = $("#prority option:selected").val();
        var call_suff = $("#call_suff").val();

//        if (getinfo == "default") {
//
//        } else {
        $.post("../GetApiListByModelNameAndUserId",
            {projectname: getinfo, userid: userid, prority: getprority, call_suff: call_suff},
            function (data) {
                if (data){
                    $("#case_list").html(data);
                } else {
                    $("#case_list").html("请选择模块");
                }
            })
//        }
    }

    function testInput() {
        var chk_value = [];
        var result = "";
        var anotherip = $("#ip_another option:selected").val();
        if (document.getElementById("ip_input").value != "") {
            anotherip = document.getElementById("ip_input").value;
        }
        if (document.getElementById("ids_input").value != "") {
            result = document.getElementById("ids_input").value;
        } else {
            alert("您没有选择任何用例");
            return false
        }
        $.post("../runmuilttest_new",
            {preids: result, another_ip: anotherip},
            function (data) {
                sendresult(data);
            }
        )


    }

    function sendresult(result) {
        document.getElementById("result_try").value = result;
        document.getElementById("try").submit();
    }

    function selectall() {
        var box = document.getElementsByName("select");
        var len = box.length;
        for (var i = 0; i < len; i++) {
            box[i].checked = true;
        }
    }

    function unselectall() {
        var box = document.getElementsByName("select");
        var len = box.length;
        for (var i = 0; i < len; i++) {
            box[i].checked = false;
        }
    }
</script>
</html>