package //Enter your package name here;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import com.talk51.Global.*;

public class //Enter your test_class name here {

	@Before
	public void setUp() throws Exception {
	}
	
	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void test() {
	String Api_url="${Api_url}";

	<#if (parmlength>0)>
	Map<String, String> parameters = new HashMap<String, String>();
	<#assign vflag = 0 >
	<#list 1..parmlength as flag>
	<#assign temp_key="parmkey"+vflag parmkey=temp_key?eval>
	<#assign temp_value="parmvalue"+vflag parmvalue=temp_value?eval>
	parameters.put("${parmkey}",""${parmvalue}");
	<#assign vflag=vflag+1>
	</#list>
	</#if>
	
	<#if (Method=="get")>
	String api_result = GetMethod.sendGet(Api_url, parameters);
	<#elseif (Method=="post")>
	String api_result = PostMethod.sendPost(Api_url, parameters);
	<#else> 
	</#if> 
	}
	
}