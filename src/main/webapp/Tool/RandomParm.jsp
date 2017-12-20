<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="java.util.*"
    import="com.talk51.Utils.*"
%>
<%
String unit_int_part_temp=request.getParameter("intunit");
String unit_dec_part_temp=request.getParameter("decunit");

if(unit_int_part_temp=="")
{
	unit_int_part_temp="5";
}
if(unit_dec_part_temp=="")
{
	unit_dec_part_temp="0";
}

int unit_int_part_random=Integer.parseInt(unit_int_part_temp);
int unit_dec_part_random=Integer.parseInt(unit_dec_part_temp);
String result=ToolForRandomParmRandomUnit.randomparm(unit_int_part_random,unit_dec_part_random);
%>

<%=result%>