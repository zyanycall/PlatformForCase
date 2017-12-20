<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    
    import="java.sql.*"
    import="java.util.*"
    import="java.io.*"
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
int user_id=0;
if(session.getAttribute("userid")==null)
	{
%>
<script language=javascript>
alert("您尚未登陆或者登陆信息已失效，请重新登陆")
</script>
<%
	response.sendRedirect("../index.html");
	return;
	}
else
	{
	Object O=session.getAttribute("userid");
	user_id=Integer.parseInt(O==null?"":O.toString());
	}

%>
</head>
<body>
<div>
请选择测试用例所属模块<br>
选择模块：<br><SELECT name="ModelSelect" id="model_name">
<OPTION value="default" selected>请选择...</OPTION>
<%
Connection connection=JDBCConnectionPool.getConnection();
Statement statement = connection.createStatement();
String sql="SELECT distinct model_name FROM interface_list";
ResultSet rs = statement.executeQuery (sql);
while(rs.next())
{
%>
<OPTION value="<%=rs.getString("model_name")%>"><%=rs.getString("model_name")%></OPTION>
<%
}
JDBCConnectionPool.close(rs, statement);
%>
</SELECT><br>
</div>
<div>
优先级
<SELECT name="prority_select" id="prority">
<OPTION value="1" selected>1</OPTION>
<OPTION value="2" >2</OPTION>
<OPTION value="3" >3</OPTION>
<OPTION value="10">10</OPTION>
<OPTION value="1,2,3,10" >全部</OPTION>
</SELECT>
<input type="button" value="刷新" onclick="refresh()">
</div>
<div id="case_list">
请选择模块
</div>
<div>
<!--<input type="button" value="批量运行所选择用例_旧方法" onclick="testselected()">-->
<input type="button" value="批量运行所选择用例" onclick="testselected_new()">
<input type="button" value="批量删除所选择用例" onclick="deleteselected()">
<input type="button" value="全选" onclick="selectall()">
<input type="button" value="取消全选" onclick="unselectall()">
<input type="button" value="刷新" onclick="refresh()">
</div>
<div>
如果需要在用例保存之外的环境运行用例，请选择<br>
已有环境：<SELECT name="ip" id="ip_another">
<OPTION value="none" selected>&lt;使用用例本身IP地址&gt;</OPTION>
<%
Connection connection2=JDBCConnectionPool.getConnection();
Statement statement2 = connection2.createStatement();
String sql2="SELECT * FROM interface_ip";
ResultSet rs2 = statement2.executeQuery (sql2);
while(rs2.next())
{
%>
<OPTION value="<%=rs2.getString("ip")%>:<%=rs2.getString("port")%>"><%=rs2.getString("ip")%>:<%=rs2.getString("port")%></OPTION>
<%
}
rs2.close(); 
statement2.close(); 
connection2.close(); 
%>
</SELECT>
<br>
</div>
<form id="try" action="../Tool/Result.jsp" method="post" target="_blank">
<input id="result_try" name="result"  type="hidden" />
</form>
</body>
<script language=javascript>
var userid=<%=user_id%>

$(document).ready(
	$("#model_name").change(
		function(){
			refresh();
	}))

    $("#prority").change(
		function(){
			refresh();
	})

//function getcaseids(){
//  var chk_value =[];
//  var	result="";
//  var anotherip=$("#ip_another option:selected").val();
//  $('input[name="select"]:checked').each(function(){
//	  result=result+($(this).val())+",";
//  });
//
//  if(result=="")
//	  {
//	  alert("您没有选择任何用例");
//	  return false
//	  }
//	$.post("../runmuilttest",
//			{preids:result,another_ip:anotherip},
//			function(data)
//				{
//					sendresult(data);
//				}
//			)
//}


function deleteselected() {
    var ids = "";
    var r=confirm("是否真的删除？");
    if (r==true){
      $('input[name="select"]:checked').each(function(){
          if (ids == "") {
              ids = $(this).val();
          } else {
              ids = ids + "," + $(this).val();
          }
      });
    }else{
      return false;
    }
	if(ids == ""){
	    alert("您没有选择任何用例");
		return false
	}
	$.post(
	    "../batchDelCases",
        {preids:ids},
        function(data){
        if(data == "error"){
            alert("删除失败！");
        } else {
            alert("删除成功！");
            location.reload();
        }
    })
}


function getcaseids_new(){
	  var chk_value =[];
	  var	result="";
	  var anotherip=$("#ip_another option:selected").val();
	  $('input[name="select"]:checked').each(function(){
		  result=result+($(this).val())+",";
	  });
	  
	  if(result=="")
		  {
		  alert("您没有选择任何用例");
		  return false
		  }
		$.post("../runmuilttest_new",
				{preids:result,another_ip:anotherip},
				function(data)
					{
					sendresult(data);
					}
				)
}

function refresh() {
    var getinfo=$("#model_name option:selected").val();
    var getprority=$("#prority option:selected").val();

    if(getinfo=="default"){
        $("#case_list").html("请选择模块");
    } else {
        $.post("../GetApiListByModelNameAndUserId",
            {projectname:getinfo,userid:userid,prority:getprority},
            function(data){
                $("#case_list").html(data);
        })
    }
}

//function testselected(){
//	getcaseids();
//}

function testselected_new(){
	getcaseids_new();
}

function sendresult(result)
{
	document.getElementById("result_try").value=result;
	document.getElementById("try").submit();
}

function selectall()
{
	var box=document.getElementsByName("select");
	var len = box.length; 
	for(var i=0; i<len; i++)
		{
		box[i].checked = true; 
		}
}

function unselectall()
{
	var box=document.getElementsByName("select");
	var len = box.length; 
	for(var i=0; i<len; i++)
		{
		box[i].checked = false; 
		}
}
</script>
</html>