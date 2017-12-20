<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="java.util.*"
    import="com.talk51.Utils.*"
%>
<%
String unit_int_part_temp=request.getParameter("intunit");
String unit_dec_part_temp=request.getParameter("decunit");
int typetrigger=Integer.parseInt(request.getParameter("typetrigger"));

String result="";

if(typetrigger==0)
{
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
result=ToolForRandomParmRandomUnit.randomparm(unit_int_part_random,unit_dec_part_random);
}
else if(typetrigger==1)
{
	if(unit_int_part_temp=="")
	{
		int r=(int)(1+Math.random()*(9+1)-1);
		switch (r)
		{
			case 0:result="This is a random string";break;
			case 1:result="Peter is ...!";break;
			case 2:result="Special mark:&*%#";break;
			case 3:result="I want go home %$";break;
			case 4:result="A space here→ ←,get it?";break;
			case 5:result="Oh!";break;
			case 6:result="Huhh...(What?)";break;
			case 7:result="Do you know?";break;
			case 8:result="Oh!You're in luck!You got a very long random string,but,is that long enough?No?Now get some special mark!How about !!#@#$^%&&*()!HaHaHa!";break;
			case 9:result="Are you a QA or a Rd?";break;
			
	}
}
	else
	{
		String basestr="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789~!@#$%^&*() ";
		int unit_int_part_random=Integer.parseInt(unit_int_part_temp);
		int length=(int)(Math.random()*(unit_int_part_random+1-1)+1);
        for(int i = 0 ; i < length; ++i){
        	int temp=(int)(Math.random()*(74-1)+1);
        	result=result+basestr.charAt(temp);
        }
	}
}
%>

<%=result%>