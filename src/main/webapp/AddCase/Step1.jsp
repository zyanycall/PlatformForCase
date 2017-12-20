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
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="../js/jquery-3.1.0.min.js"></script>
<title>添加测试用例</title>
<%
request.setCharacterEncoding("UTF-8");

%>
</head>
<body>
<div id="all" style="margin-left:10%;margin-right:10%;position:relative;">
<div id="part1" style="border:1px solid #000;padding:4px">
<p style="letter-spacing:2px">第一步，请输入要添加测试用例所属的模块，如果模块存在，也可以直接添加（特殊限制：模块名称中不要包含:英文冒号字符）<br>
注：如果模块不存在，当提交之后会自动添加至系统，添加后刷新即可在已有模块中看到</p><br>
模块名称：<input type="text" id="model_name" name="model_name" onchange="forstep2_manual()" />
已有模块：<SELECT name="districtSelect" onchange="forstep2_auto()" id="model_name_auto">
<OPTION value="" selected>请选择...</OPTION>
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
<div id="part2" style="border:1px solid #000;padding:4px">
<p style="letter-spacing:2px">第二步，请输入要添加测试用例的目标接口，如果模块中该存在，也可以直接添加</p><br>
接口名称：<input type="text" id="interface_name" name="interface_name" />
接口方法：<input type="text" id="interface_suff" name="interface_suff" />
<script language=javascript>
function starthover(){
    var posX,posY;
    if (window.innerHeight) {
        posX = window.pageXOffset;
        posY = window.pageYOffset;
    }
else if (document.documentElement && document.documentElement.scrollTop) {
posX = document.documentElement.scrollLeft;
posY = document.documentElement.scrollTop;
}
else if (document.body) {
posX = document.body.scrollLeft;
posY = document.body.scrollTop;
    }
var ad=document.getElementById("hover section");
	ad.style.position=("fixed");
	ad.style.backgroundColor=("white");
    ad.style.top=(280)+"px";
    ad.style.left=(830)+"px";
    t=setTimeout("starthover()",100);
}

function stop(){
	clearTimeout(t);
	var ad=document.getElementById("hover section");
	ad.style.position=("static");
}

var step2len;
function forstep2_manual()
{
	var alerttest=document.getElementById("model_name").value;
	var tflag=alerttest.indexOf(":");

	
	if(tflag>-1)
	{
		alert("您输入的模块名称中包含英文冒号字符，会导致程序错误，请更换");
		document.getElementById('model_name').value="";
		return false
	}
	
	getModelName();
}
function forstep2_auto()
{
	document.getElementById('model_name').value=document.getElementById('model_name_auto').options[document.getElementById('model_name_auto').selectedIndex].value
	getModelName()
}

function getModelName()
{
	var modelname=document.getElementById("model_name").value;
	var xmlhttp;
	var getresponse;
	
	if (window.XMLHttpRequest)
	  {// code for IE7+, Firefox, Chrome, Opera, Safari
	  xmlhttp=new XMLHttpRequest();
	  }
	else
	  {// code for IE6, IE5
	  xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
	  }
	
	xmlhttp.onreadystatechange=function()
	{
		if (xmlhttp.readyState==4 && xmlhttp.status==200)
		{
			getresponse=xmlhttp.responseText;
			getresponse=getresponse.replace(/\s+/g,"");
			getresponse=getresponse.substring(0,getresponse.length-1);
			var fin_result=getresponse.split(":");
			step2len=fin_result.length/2;
			setstep2Option(step2len,fin_result);
		}
	}
	xmlhttp.open("GET","../Controller/GetApiFromModelName.jsp?modelname="+modelname+"",true);
	xmlhttp.send();
}

function setstep2Option(optionnum,optionInfo)
{
	document.getElementById("step2Select").options.length=1;
	
	if(optionnum<1)
		{
		return false;
		}
	else
		{
		for(var j=0;j<optionnum;j++)
			{
				var newSelectOption = document.createElement("option");
				newSelectOption.id = "SelectOption"+j;
				newSelectOption.innerText = optionInfo[j*2]+":"+optionInfo[j*2+1];
				newSelectOption.value = optionInfo[j*2]+":"+optionInfo[j*2+1];
				document.getElementById("step2Select").appendChild(newSelectOption);
			}
		}
	
}

function forstep2_full()
{
	var temp=document.getElementById('step2Select').options[document.getElementById('step2Select').selectedIndex].value
	var temp1=temp.split(":");
	var part1=temp1[0];
	var part2=temp1[1];
	document.getElementById('interface_name').value=part1;
	if(document.getElementById('step2Select').options[document.getElementById('step2Select').selectedIndex].value=="")
		{
		document.getElementById('interface_suff').value="";
		}
	else
	{
		document.getElementById('interface_suff').value=part2;
	}
}
</script>
<br>已有接口：<br><SELECT name="districtSelect" id="step2Select" onchange="forstep2_full()">
<OPTION value="" selected>请选择...</OPTION>
</SELECT><br>
<script language=javascript>

var parmblockid=0;

function GetRandomValue(id)
{
	var cut="randombtn";
	var f_id=id.split(cut)[1]; 
	
	var intunit=document.getElementById("intunit"+f_id).value;
	var decunit=document.getElementById("decunit"+f_id).value;

	if(isNaN(intunit)||isNaN(decunit))
		{
		alert("随机参数位数，请输入整数");
		return false;
		}
	
	var xmlhttp;
	if (window.XMLHttpRequest)
	  {// code for IE7+, Firefox, Chrome, Opera, Safari
	  xmlhttp=new XMLHttpRequest();
	  }
	else
	  {// code for IE6, IE5
	  xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
	  }
	xmlhttp.onreadystatechange=function()
	{
	if (xmlhttp.readyState==4 && xmlhttp.status==200)
	  {
		document.getElementById("value"+f_id).value=xmlhttp.responseText;
	  }
	}
	xmlhttp.open("GET","../Tool/RandomParm.jsp?intunit="+intunit+"&decunit="+decunit+"",true);
	xmlhttp.send();
	
}

function addParmTeam()
{
	parmblockid++;
	
	var newParmTeam_name = document.createElement("input");
	newParmTeam_name.type="text";
	newParmTeam_name.id="name"+parmblockid; 
	document.getElementById("dynamic parm").appendChild(newParmTeam_name);
	
	var newParmTeam_value = document.createElement("input");
	newParmTeam_value.type="text";
	newParmTeam_value.id="value"+parmblockid; 
	document.getElementById("dynamic parm").appendChild(newParmTeam_value);
	
	var newParmTeam_randombtn = document.createElement("input");
	newParmTeam_randombtn.type="button";
	newParmTeam_randombtn.value="随机生成该参数";
	newParmTeam_randombtn.onclick=function() {GetRandomValue(this.id);};
	newParmTeam_randombtn.id="randombtn"+parmblockid; 
	document.getElementById("dynamic parm").appendChild(newParmTeam_randombtn);
	
	var newParmTeam_intunit = document.createElement("input");
	newParmTeam_intunit.type="text";
	newParmTeam_intunit.id="intunit"+parmblockid; 
	document.getElementById("dynamic parm").appendChild(newParmTeam_intunit);
	
	var newParmTeam_decunit = document.createElement("input");
	newParmTeam_decunit.type="text";
	newParmTeam_decunit.id="decunit"+parmblockid; 
	document.getElementById("dynamic parm").appendChild(newParmTeam_decunit);
	
	var newParmTeam_per_0 = document.createElement("input");
	newParmTeam_per_0.type="radio";
	newParmTeam_per_0.name="parm_per"+parmblockid;
	newParmTeam_per_0.value="0";
	newParmTeam_per_0.id="parm_per_0_"+parmblockid; 
	document.getElementById("dynamic parm").appendChild(newParmTeam_per_0);

	var newParmTeam_per_50 = document.createElement("input");
	newParmTeam_per_50.type="radio";
	newParmTeam_per_50.name="parm_per"+parmblockid;
	newParmTeam_per_50.value="1";
	newParmTeam_per_50.id="parm_per_50_"+parmblockid; 
	document.getElementById("dynamic parm").appendChild(newParmTeam_per_50);

	var newParmTeam_per_100 = document.createElement("input");
	newParmTeam_per_100.type="radio";
	newParmTeam_per_100.name="parm_per"+parmblockid;
	newParmTeam_per_100.checked="checked";
	newParmTeam_per_100.value="2";
	newParmTeam_per_100.id="parm_per_100_"+parmblockid; 
	document.getElementById("dynamic parm").appendChild(newParmTeam_per_100);
	
	var newParmTeam_end = document.createElement("br");
	newParmTeam_end.id="endtag"+parmblockid; 
	document.getElementById("dynamic parm").appendChild(newParmTeam_end);
}

function deleteParmTeam()
{
	if(parmblockid==0)
		{
		alert("已经没有参数组了");
		return false
		}
	
	var block_name= document.getElementById("name"+parmblockid);
	block_name.parentNode.removeChild(block_name);
	
	var block_value= document.getElementById("value"+parmblockid);
	block_value.parentNode.removeChild(block_value);
	
	var block_button= document.getElementById("randombtn"+parmblockid);
	block_button.parentNode.removeChild(block_button);
	
	var block_intunit= document.getElementById("intunit"+parmblockid);
	block_intunit.parentNode.removeChild(block_intunit);
	
	var block_decunit= document.getElementById("decunit"+parmblockid);
	block_decunit.parentNode.removeChild(block_decunit);
	
	var block_parmper0= document.getElementById("parm_per_0_"+parmblockid);
	block_parmper0.parentNode.removeChild(block_parmper0);
	
	var block_parmper50= document.getElementById("parm_per_50_"+parmblockid);
	block_parmper50.parentNode.removeChild(block_parmper50);
	
	var block_parmper100= document.getElementById("parm_per_100_"+parmblockid);
	block_parmper100.parentNode.removeChild(block_parmper100);
	
	var block_end_tag= document.getElementById("endtag"+parmblockid);
	block_end_tag.parentNode.removeChild(block_end_tag);
	
	parmblockid--;
}
</script>
</div>
<div id="part3" style="border:1px solid #000;padding:4px">
<p style="letter-spacing:2px">第三步，请添加该接口所需要的参数名，以及参数值，第一个输入框请输入参数名，第二个为参数值。<br>如果参数类型为整形或浮点型，可随机生成，请在第三个框中输入随机整数位数，在第四个框中输入随机小数位数<br>后三个按钮为参数传递概率，第一个按钮为0%，表示该参数必定不传，第二个按钮为50%，表示该参数有50%会不传，第三个为100%，表示该参数必传<br></p>
<input type="button" value="添加新参数" onclick="addParmTeam()"/>
<input type="button" value="删除一组参数" onclick="deleteParmTeam()"/>
<div id="dynamic parm"></div>
</div>
<div id="part4" style="border:1px solid #000;padding:4px">
<p style="letter-spacing:2px">第四步，请选择目标接口部署的环境地址以及端口号</p><br>
已有环境：<SELECT name="ip" id="ip_auto">
<%
Connection connection2=JDBCConnectionPool.getConnection();
Statement statement2 = connection2.createStatement();
String sql2="SELECT * FROM interface_ip";
ResultSet rs2 = statement2.executeQuery (sql2);
while(rs2.next())
{
	String ip=rs2.getString("ip");
	String port=rs2.getString("port");
	%><OPTION value="<%=ip%>:<%=port%>"><%=ip%>:<%=port%></OPTION><%	
}
JDBCConnectionPool.close(rs2, statement2);
%>
</SELECT>
<br>
</div>
<div id="part5" style="border:1px solid #000;padding:4px">
第五步，请选择目标接口的请求方法<br>
<input name="method" type="radio" value="get" />Get
<input name="method" type="radio" value="post" checked/>Post
<input name="method" type="radio" value="post_body"/>Post_Body
<input name="method" type="radio" value="post_return_json"/>Post_Return_Json
<input name="method" type="radio" value="put"/>Put
<input name="method" type="radio" value="delete"/>Delete
</div>
<div id="part6" style="border:1px solid #000;padding:4px">
第六步，请选择测试用例的优先级<br>
<input name="priority" type="radio" value="1" />1:第一次冒烟测试和回归测试时需要执行<br>
<input name="priority" type="radio" value="2" />2:效验接口参数合法性<br>
<input name="priority" type="radio" value="3" />3:自定义<br>
<input name="priority" type="radio" value="10" checked/>10:人工执行，比较实际结果和预期结果<br>
</div>
<div style="position:relative">
<div id="hover section" style="position:static">
<div id="part7" style="border:1px solid #000;padding:4px">
第七步，请输入预期结果，并选择该接口是否有缓存<br>
<input type="submit" value="浮动该层" onclick="starthover()"/>
<input type="submit" value="停止浮动" onclick="stop()"/>
<br>
<textarea name="expected" id="expected" style="width:500px;height:80px"/></textarea><br>
是否有缓存<input type="checkbox" id="cacheflag" >
</div>
<div id="part8" style="border:1px solid #000;padding:4px">
第八步，请输入测试用例描述<br>
<textarea name="scenario" id="scenario" style="width:500px;height:80px"/></textarea><br>
</div>
<div id="part9" style="border:1px solid #000;padding:4px">
第九步，提交该用例、立即执行或生成junit测试类代码<br>
<input type="button" value="提交" onclick="testsumbit()">
<input type="button" value="现在执行" onclick="doitnow()">
<input type="button" value="生成junit代码" onclick="junitgen()">
<br>
<script language=javascript>
function datatoJson(value)
{
	var result;
	try
	{
		result=JSON.parse(value);
	}
	catch(e)
	{
		result=value;
	}
	return result;
}

function stringToHex(str)
{
var val="";
for(var i = 0; i < str.length; i++){
//if(val == "")
//	{
//	val = "00"+str.charCodeAt(i).toString(16);
//	}
//else
//	{
	valtmp =	str.charCodeAt(i).toString(16);
	if(valtmp.length==1)
		{
		valtmp="000"+valtmp;
		}
	else if(valtmp.length==2)
		{
		valtmp="00"+valtmp;
		}
	val=val+valtmp
//	}
}
return val;
}

function testsumbit()
{
	var bodyinfo="";
	var bodyinfo2="";
	var bodyobject=new Object;
	var tempbody = new Array();
	var section2parm={};
	var section2parm_uncode={};
	
	var st1=document.getElementById("model_name").value;
	var st2=document.getElementById("interface_name").value;
	var st3=document.getElementById("interface_suff").value;
	var st4=document.getElementById("ip_auto").value;
	
	var st5=$('input[name="method"]:checked').val();
    
    var st6=$('input[name="priority"]:checked').val();
    var st7=document.getElementById("expected").value
    var st8=document.getElementById("scenario").value
    var st9
    
    if ($('#cacheflag').is(':checked'))
    {
    	st9=1;
    }
    else
	{
		st9=0
	}
    
    if(st5!="post_body")
    	{
    	if(parmblockid)
    	{
    	for (var k=1;k<=parmblockid;k++)
    		{
    		var testd=$('input[name="parm_per'+k+'"]:checked').val();
    		
    		if(testd==0)
			{
			tempbody[k-1]="";
			}
		else if(testd==1)
			{
			var random=Math.floor(Math.random()*2);
			
			if(random==0)
				{
				tempbody[k-1]="";
				}
			else if(random==1)
				{
				tempbody[k-1]=document.getElementById("name"+k).value+"="+bodyinfo+encodeURIComponent(document.getElementById("value"+k).value)+"&";
				section2parm[document.getElementById("name"+k).value]=bodyinfo+document.getElementById("value"+k).value;
				section2parm[document.getElementById("name"+k).value]=stringToHex(section2parm[document.getElementById("name"+k).value]);
				section2parm[document.getElementById("name"+k).value]=encodeURIComponent(section2parm[document.getElementById("name"+k).value]);
				}
			}
		else if(testd==2)
			{
			tempbody[k-1]=document.getElementById("name"+k).value+"="+bodyinfo+encodeURIComponent(document.getElementById("value"+k).value)+"&";
			section2parm[document.getElementById("name"+k).value]=bodyinfo+document.getElementById("value"+k).value;
			section2parm[document.getElementById("name"+k).value]=stringToHex(section2parm[document.getElementById("name"+k).value]);
			section2parm[document.getElementById("name"+k).value]=encodeURIComponent(section2parm[document.getElementById("name"+k).value]);
			}
    		}
    	}
    	
    	for (var l=0;l<tempbody.length;l++)
		{
		bodyinfo=bodyinfo+tempbody[l]
		}
    	
    	bodyinfo=bodyinfo.substring(0,bodyinfo.length-1);
    	bodyinfos2=JSON.stringify(section2parm);
    	}
    else if(st5=="post_body")
    	{
    	if(parmblockid)
    	{
    		for (var k=1;k<=parmblockid;k++)
    			{
    			var testd=$('input[name="parm_per'+k+'"]:checked').val();

    		if(testd==0)
			{
			bodyobject[document.getElementById("name"+k).value]=datatoJson("");
			}
		else if(testd==1)
			{
			var random=Math.floor(Math.random()*2);
			
			if(random==0)
				{
				bodyobject[document.getElementById("name"+k).value]=datatoJson("");
				}
			else if(random==1)
				{
				bodyobject[document.getElementById("name"+k).value]=datatoJson(document.getElementById("value"+k).value);
				section2parm[document.getElementById("name"+k).value]=bodyinfo+document.getElementById("value"+k).value;
				section2parm[document.getElementById("name"+k).value]=stringToHex(section2parm[document.getElementById("name"+k).value]);
				section2parm[document.getElementById("name"+k).value]=encodeURIComponent(section2parm[document.getElementById("name"+k).value]);				
				}
			}
		else if(testd==2)
			{
			bodyobject[document.getElementById("name"+k).value]=datatoJson(document.getElementById("value"+k).value);
			section2parm[document.getElementById("name"+k).value]=bodyinfo+document.getElementById("value"+k).value;
			section2parm[document.getElementById("name"+k).value]=stringToHex(section2parm[document.getElementById("name"+k).value]);
			section2parm[document.getElementById("name"+k).value]=encodeURIComponent(section2parm[document.getElementById("name"+k).value]);			
			}
    			}
    	}
    	bodyinfo=JSON.stringify(bodyobject);
    	bodyinfos2=JSON.stringify(section2parm);
    	}
    
	$.post("../Controller/AddCaseApi.jsp",
			{model_name:st1,interface_name:st2,interface_suff:st3,host_ip:st4,method:st5,priority:st6,expected_result:st7,scenario:st8,body:encodeURIComponent(bodyinfo),is_cache:st9,section2body:bodyinfos2},
			function(data)
				{
					alert("添加用例成功");
				}
			)
}

function getTimeGenId()
{
	var r_id = (new Date()).valueOf();
	r_id=r_id.toString();
	r_id=r_id.substr(r_id.length-8);
	return r_id;
}

function doitnow()
{
	var bodyinfo="";
	var bodyobject=new Object;
	var tempbody = new Array();
	var section2parm={};
	
	var stn1=document.getElementById("ip_auto").value
	var stn2=document.getElementById("interface_suff").value
	var url=stn1+stn2
	var stn5
	
	var methodflag = document.getElementsByName("method");
	
	var bodyinfo="";
	var tempbody = new Array();
	
    for(var ri=0;ri<methodflag.length;ri++){
        if(methodflag[ri].checked)
        {
            stn5=methodflag[ri].value
        }
        }
	
    var DoNowFlag=0;
    if(stn5=="get")
    	{
    	DoNowFlag=0;
    	}
    else if(stn5=="post")
    	{
    	DoNowFlag=1;
    	}
    else if(stn5=="post_body")
	{
	DoNowFlag=2;
	}
    else if(stn5=="post_return_json")
	{
	DoNowFlag=3;
	}
    else if(stn5=="put")
	{
	DoNowFlag=4;
	}
    else if(stn5=="delete")
	{
	DoNowFlag=5;
	}
    
    if(stn5!="post_body")
	{
	if(parmblockid)
	{
	for (var k=1;k<=parmblockid;k++)
		{
		var testd=$('input[name="parm_per'+k+'"]:checked').val();
		
		if(testd==0)
		{
		tempbody[k-1]="";
		}
	else if(testd==1)
		{
		var random=Math.floor(Math.random()*2);
		
		if(random==0)
			{
			tempbody[k-1]="";
			}
		else if(random==1)
			{
			tempbody[k-1]=document.getElementById("name"+k).value+"="+bodyinfo+encodeURIComponent(document.getElementById("value"+k).value)+"&";
			section2parm[document.getElementById("name"+k).value]=bodyinfo+document.getElementById("value"+k).value;
			section2parm[document.getElementById("name"+k).value]=stringToHex(section2parm[document.getElementById("name"+k).value]);
			section2parm[document.getElementById("name"+k).value]=encodeURIComponent(section2parm[document.getElementById("name"+k).value]);
			}
		}
	else if(testd==2)
		{
		tempbody[k-1]=document.getElementById("name"+k).value+"="+bodyinfo+encodeURIComponent(document.getElementById("value"+k).value)+"&";
		section2parm[document.getElementById("name"+k).value]=bodyinfo+document.getElementById("value"+k).value;
		section2parm[document.getElementById("name"+k).value]=stringToHex(section2parm[document.getElementById("name"+k).value]);
		section2parm[document.getElementById("name"+k).value]=encodeURIComponent(section2parm[document.getElementById("name"+k).value]);
		}
		}
	}
	
	for (var l=0;l<tempbody.length;l++)
	{
	bodyinfo=bodyinfo+tempbody[l]
	}
	
	bodyinfo=bodyinfo.substring(0,bodyinfo.length-1);
	}
else if(stn5=="post_body")
	{
	if(parmblockid)
	{
		for (var k=1;k<=parmblockid;k++)
			{
			var testd=$('input[name="parm_per'+k+'"]:checked').val();

		if(testd==0)
		{
		bodyobject[document.getElementById("name"+k).value]=datatoJson("");
		}
	else if(testd==1)
		{
		var random=Math.floor(Math.random()*2);
		
		if(random==0)
			{
			bodyobject[document.getElementById("name"+k).value]=datatoJson("");
			}
		else if(random==1)
			{
			bodyobject[document.getElementById("name"+k).value]=datatoJson(document.getElementById("value"+k).value);
			section2parm[document.getElementById("name"+k).value]=bodyinfo+document.getElementById("value"+k).value;
			section2parm[document.getElementById("name"+k).value]=stringToHex(section2parm[document.getElementById("name"+k).value]);
			section2parm[document.getElementById("name"+k).value]=encodeURIComponent(section2parm[document.getElementById("name"+k).value]);				
			}
		}
	else if(testd==2)
		{
		bodyobject[document.getElementById("name"+k).value]=datatoJson(document.getElementById("value"+k).value);
		section2parm[document.getElementById("name"+k).value]=bodyinfo+document.getElementById("value"+k).value;
		section2parm[document.getElementById("name"+k).value]=stringToHex(section2parm[document.getElementById("name"+k).value]);
		section2parm[document.getElementById("name"+k).value]=encodeURIComponent(section2parm[document.getElementById("name"+k).value]);			
		}
			}
	}
	bodyinfo=JSON.stringify(bodyobject);
	}
    
	bodyinfo=bodyinfo.replace("idgenerator",getTimeGenId());
	alert(getTimeGenId());
	
    var xmlhttp;
	if (window.XMLHttpRequest)
	  {// code for IE7+, Firefox, Chrome, Opera, Safari
	  xmlhttp=new XMLHttpRequest();
	  }
	else
	  {// code for IE6, IE5
	  xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
	  }
	xmlhttp.onreadystatechange=function()
	{
	if (xmlhttp.readyState==4 && xmlhttp.status==200)
		{
//		alert("测试用例运行成功");
		var save=xmlhttp.responseText;
		sendresult(save);
		}
	}
	xmlhttp.open("POST","../Tool/RunCaseNow.jsp",true);
	xmlhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
	xmlhttp.send("url="+encodeURIComponent(url)+"&bodyinfo="+encodeURIComponent(bodyinfo)+"&method="+DoNowFlag);
}

function sendresult(result)
{
	document.getElementById("result_try").value=result;
	document.getElementById("try").submit();
}

function junitgen()
{
	var bodyinfo="";
	var bodyinfo2="";
	var bodyobject=new Object;
	var tempbody = new Array();
	var section2parm={};
	var section2parm_uncode={};
	
	var st1=document.getElementById("model_name").value;
	var st2=document.getElementById("interface_name").value;
	var st3=document.getElementById("interface_suff").value;
	var st4=document.getElementById("ip_auto").value;
	
	var st5=$('input[name="method"]:checked').val();
    
    var st6=$('input[name="priority"]:checked').val();
    var st7=document.getElementById("expected").value
    var st8=document.getElementById("scenario").value
    var st9
    
    if ($('#cacheflag').is(':checked'))
    {
    	st9=1;
    }
    else
	{
		st9=0
	}
    
    if(st5!="post_body")
    	{
    	if(parmblockid)
    	{
    	for (var k=1;k<=parmblockid;k++)
    		{
    		var testd=$('input[name="parm_per'+k+'"]:checked').val();
    		
    		if(testd==0)
			{
			tempbody[k-1]="";
			}
		else if(testd==1)
			{
			var random=Math.floor(Math.random()*2);
			
			if(random==0)
				{
				tempbody[k-1]="";
				}
			else if(random==1)
				{
				tempbody[k-1]=document.getElementById("name"+k).value+"="+bodyinfo+encodeURIComponent(document.getElementById("value"+k).value)+"&";
				section2parm[document.getElementById("name"+k).value]=bodyinfo+document.getElementById("value"+k).value;
				section2parm[document.getElementById("name"+k).value]=stringToHex(section2parm[document.getElementById("name"+k).value]);
				section2parm[document.getElementById("name"+k).value]=encodeURIComponent(section2parm[document.getElementById("name"+k).value]);
				}
			}
		else if(testd==2)
			{
			tempbody[k-1]=document.getElementById("name"+k).value+"="+bodyinfo+encodeURIComponent(document.getElementById("value"+k).value)+"&";
			section2parm[document.getElementById("name"+k).value]=bodyinfo+document.getElementById("value"+k).value;
			section2parm[document.getElementById("name"+k).value]=stringToHex(section2parm[document.getElementById("name"+k).value]);
			section2parm[document.getElementById("name"+k).value]=encodeURIComponent(section2parm[document.getElementById("name"+k).value]);
			}
    		}
    	}
    	
    	for (var l=0;l<tempbody.length;l++)
		{
		bodyinfo=bodyinfo+tempbody[l]
		}
    	
    	bodyinfo=bodyinfo.substring(0,bodyinfo.length-1);
    	bodyinfos2=JSON.stringify(section2parm);
    	}
    else if(st5=="post_body")
    	{
    	if(parmblockid)
    	{
    		for (var k=1;k<=parmblockid;k++)
    			{
    			var testd=$('input[name="parm_per'+k+'"]:checked').val();

    		if(testd==0)
			{
			bodyobject[document.getElementById("name"+k).value]=datatoJson("");
			}
		else if(testd==1)
			{
			var random=Math.floor(Math.random()*2);
			
			if(random==0)
				{
				bodyobject[document.getElementById("name"+k).value]=datatoJson("");
				}
			else if(random==1)
				{
				bodyobject[document.getElementById("name"+k).value]=datatoJson(document.getElementById("value"+k).value);
				section2parm[document.getElementById("name"+k).value]=bodyinfo+document.getElementById("value"+k).value;
				section2parm[document.getElementById("name"+k).value]=stringToHex(section2parm[document.getElementById("name"+k).value]);
				section2parm[document.getElementById("name"+k).value]=encodeURIComponent(section2parm[document.getElementById("name"+k).value]);				
				}
			}
		else if(testd==2)
			{
			bodyobject[document.getElementById("name"+k).value]=datatoJson(document.getElementById("value"+k).value);
			section2parm[document.getElementById("name"+k).value]=bodyinfo+document.getElementById("value"+k).value;
			section2parm[document.getElementById("name"+k).value]=stringToHex(section2parm[document.getElementById("name"+k).value]);
			section2parm[document.getElementById("name"+k).value]=encodeURIComponent(section2parm[document.getElementById("name"+k).value]);			
			}
    			}
    	}
    	bodyinfo=JSON.stringify(bodyobject);
    	bodyinfos2=JSON.stringify(section2parm);
    	}
    
	$.post("../JunitGen",
			{model_name:st1,interface_name:st2,interface_suff:st3,host_ip:st4,method:st5,priority:st6,expected_result:st7,scenario:st8,body:encodeURIComponent(bodyinfo),is_cache:st9,section2body:bodyinfos2},
			function(data)
				{
				alert(data);
				sendresult(data);
				}
			)
}
</script>
</div>
</div>
</div>
</div>
<form id="try" action="../Tool/Result.jsp" method="post" target="_blank">
<input id="result_try" name="result"  type="hidden" />
</form>
</body>
</html>