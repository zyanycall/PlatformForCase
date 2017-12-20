package com.servlet;

import com.talk51.Utils.*;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringEscapeUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

/**
 * Servlet implementation class RunMuiltTest
 */
@WebServlet(name = "RunMuiltTest_new", value = "/runmuilttest_new")
public class RunMuiltTest_new extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String actual_result = null;
        String debug = null;
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String caseids = request.getParameter("preids");
        String caseip = request.getParameter("another_ip");
        String testurl = "";
        PrintWriter out = response.getWriter();
        String[] preidquery = caseids.split(",");
        Connection connection = JDBCConnectionPool.getConnection();
        try {
            for (int i = 0; i < preidquery.length; i++) {
                Statement statement = connection.createStatement();
                Statement statementbody = connection.createStatement();
                String sql = "SELECT interface_name,host_ip,call_suff,body,scenario,method FROM cases where id = " + preidquery[i] + "";
                String sqlgetbody = "SELECT parm_relation FROM cases_parm_relation where case_id = " + preidquery[i] + "";
                ResultSet rs = statement.executeQuery(sql);
                ResultSet rsbody = statementbody.executeQuery(sqlgetbody);
                while (rs.next()) {
                    HashMap<String, String> dataparm = new HashMap<String, String>();
                    out.println("第" + preidquery[i] + "条用例运行结果:<br>");
                    out.println("所请求接口名称:<br>");
                    String testname = rs.getString("interface_name");
                    out.println(testname);
                    out.println("<br>");
                    out.println("所请求接口url和参数:<br>");
                    if (caseip.equals("none")) {
                        testurl = rs.getString("host_ip") + rs.getString("call_suff");
                    } else {
                        testurl = caseip + rs.getString("call_suff");
                    }
                    String testbody = rs.getString("body");
                    out.println(testurl + "?" + testbody);
                    out.println("<br>");
                    out.println("所请求接口参数值明细:<br>");
                    while (rsbody.next()) {
                        String testbodybuff = URLDecoder.decode(rsbody.getString("parm_relation"), "UTF-8");
                        debug = testbodybuff;
                        JSONObject temp = JSONObject.fromObject(testbodybuff);
                        for (Object k : temp.keySet()) {
                            Object v = temp.get(k);
                            dataparm.put(k.toString(), StringTool.toStingHex(v.toString()));
                        }
                    }
                    rsbody.close();
                    out.println(testbody);
                    out.println("<br>");
                    out.println("所请求接口方法:<br>");
                    String method = rs.getString("method");
                    out.println(method);
                    out.println("<br>");
                    if (method.equals("get")) {
                        actual_result = GetMethod_Old.sendGet(testurl, dataparm);
                    } else if (method.equals("post")) {
                        for (String value : dataparm.values()) {
                            try {
                                if (dataparm.get("id").equals("idgenerator")) {
                                    dataparm.put("id", GenTool.GetTimeGenId());
                                }
                            } catch (Exception e) {
                            }
                        }
                        actual_result = PostMethod_Old.sendPost(testurl, dataparm);
                    } else if (method.equals("post_body")) {
                        HashMap headersJsonMap = new HashMap();
                        headersJsonMap.put("Content-Type", "application/json");
                        actual_result = PostUtil.sendPost(headersJsonMap, testurl, rs.getString("body"));
                    } else if (method.equals("post_return_json")) {
                        for (String value : dataparm.values()) {
                            try {
                                if (dataparm.get("id").equals("idgenerator")) {
                                    dataparm.put("id", GenTool.GetTimeGenId());
                                }
                            } catch (Exception e) {
                            }
                        }
                        actual_result = PostMethod_application_json_mulit.sendPost(testurl, dataparm);
                    } else if (method.equals("put")) {
                        for (String value : dataparm.values()) {
                            try {
                                if (dataparm.get("id").equals("idgenerator")) {
                                    dataparm.put("id", GenTool.GetTimeGenId());
                                }
                            } catch (Exception e) {
                            }
                        }
                        actual_result = PutMethod_mulit.sendPut(testurl, dataparm);
                    } else if (method.equals("delete")) {
                        actual_result = DeleteMethod.sendDelete(testurl, rs.getString("body"));
                    }
                    out.println("所执行测试场景:<br>");
                    String testscr = rs.getString("scenario");
                    out.println(testscr);
                    out.println("<br>");
                    out.println("所得结果:<br>");
                    out.println(actual_result);
                    out.println("<br>");
                    out.println("<br>");
                }
                actual_result = StringEscapeUtils.escapeSql(actual_result);
                String updata_sql = "update cases set actual_result='" + actual_result + "' where id=" + preidquery[i] + "";
                statement.executeUpdate(updata_sql);
                statement.close();
                statementbody.close();
                rs.close();
            }
            out.flush();
        } catch (SQLException se) {
            // 处理 JDBC 错误
            se.printStackTrace();
        } catch (Exception e) {
            // 处理 Class.forName 错误
            e.printStackTrace();
        } finally {
            out.close();
            JDBCConnectionPool.closeConnection();
        }
    }


}
