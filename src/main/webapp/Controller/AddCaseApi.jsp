<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="java.sql.*"
    import="java.net.URLDecoder"
    import="java.net.URLEncoder"
    import="java.util.*"
    import="java.io.*"
    import="org.apache.commons.lang.StringEscapeUtils"
    import="com.talk51.Utils.JDBCConnectionPool"
    %>
<%

request.setCharacterEncoding("UTF-8");
String model_name=request.getParameter("model_name");
String interface_name=request.getParameter("interface_name");
String interface_suff=request.getParameter("interface_suff");
String host_ip=request.getParameter("host_ip");
String priority=request.getParameter("priority");
String expected_result=request.getParameter("expected_result");
String scenario=request.getParameter("scenario");
String method=request.getParameter("method");
String body=URLDecoder.decode(request.getParameter("body"),"UTF-8");
String section2body=URLEncoder.encode(request.getParameter("section2body"),"UTF-8");
String is_cache=request.getParameter("is_cache");
String section2id="";
body=StringEscapeUtils.escapeSql(body);

//Object object=session.getAttribute("userid");
//int user_id=Integer.parseInt(object==null ? "0":object.toString());

Connection connection=JDBCConnectionPool.getConnection();
Statement statement = connection.createStatement();

String insert_sql="insert into cases (project_name,interface_name,call_suff,host_ip,priority,expected_result,scenario,method,body,operator,is_cache,is_deleted) values('"+model_name+"','"+interface_name+"','"+interface_suff+"','"+host_ip+"','"+priority+"','"+expected_result+"','"+scenario+"','"+method+"','"+body+"',"+3+","+is_cache+",0)";
statement.executeUpdate(insert_sql);
ResultSet rsFlag=statement.executeQuery("SELECT @@IDENTITY AS currentID");
if(rsFlag.next()){
section2id=rsFlag.getString("currentID");
}

Statement s2statement = connection.createStatement();
String s2insert_sql="insert into cases_parm_relation (case_id,parm_relation,is_deleted) values ("+section2id+",'"+section2body+"',0)";
s2statement.executeUpdate(s2insert_sql);

Statement statement2 = connection.createStatement();
String check_sql="select * from interface_list where call_Stuff = '"+interface_suff+"' and interface_name = '"+interface_name+"' and model_name='"+model_name+"'";
ResultSet rs = statement2.executeQuery (check_sql);

if (!rs.next())
{
	String new_insert_interface_list_sql="insert into interface_list (model_name,interface_name,call_Stuff) values ('"+model_name+"','"+interface_name+"','"+interface_suff+"')";
	statement2.executeUpdate(new_insert_interface_list_sql);
}

JDBCConnectionPool.close(s2statement);
JDBCConnectionPool.close(rsFlag, statement);
JDBCConnectionPool.close(rs, statement2);
%>