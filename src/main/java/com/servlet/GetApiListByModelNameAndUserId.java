package com.servlet;

import com.talk51.Utils.JDBCConnectionPool;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Servlet implementation class GetApiListByModelNameAndUserId
 */
@WebServlet(name = "GetApiListByModelNameAndUserId", value = "/GetApiListByModelNameAndUserId")
public class GetApiListByModelNameAndUserId extends HttpServlet {
    private static final long serialVersionUID = 1L;
    int rownum = 0;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String userid = request.getParameter("userid");
        String projectname = request.getParameter("projectname");
        String prority = request.getParameter("prority");
        String callSuff = request.getParameter("call_suff");

        String sql = "SELECT * FROM cases where operator in (" + userid + ",0) and project_name = '" + projectname + "' and is_deleted <> 1 and priority in (" + prority + ")";
        if (StringUtils.isNotEmpty(callSuff)) {
            sql = "SELECT * FROM cases where is_deleted <> 1 and call_suff like '%" + callSuff +"%'";
            if (!"default".equalsIgnoreCase(projectname)) {
                sql = "SELECT * FROM cases where is_deleted <> 1 and project_name = '" + projectname + "' and call_suff like '%" + callSuff +"%'";
            }
        } else if ("default".equalsIgnoreCase(projectname)) {
            return;
        }
        PrintWriter out = response.getWriter();
        //out.println("<table border=\"1\" id=\"list_table\"><tr><th>id</th><th>接口名称</th><th>接用例场景描述</th><th>接口请求方法</th><th>接口请求地址ip以及端口</th><th>接口方法名</th><th>接口参数体</th></tr></table>");
        Connection connection = JDBCConnectionPool.getConnection();
        Statement statement = null;
        ResultSet rs = null;
        try {
            statement = connection.createStatement();
            //userID = 0 意味着所有的用户都可以看到并修改
            rs = statement.executeQuery(sql);
            out.println("<table border=\"1\" id=\"list_t\" style=\"table-layout: fixed;width: 100%;\"> ");
            out.println("<tr>");
            out.println("<th width=\"60px\">用例id</th>");
            out.println("<th>用例接口名称</th>");
            out.println("<th>用例场景描述名称</th>");
            out.println("<th width=\"60px\">用例请求方法</th>");
            out.println("<th>用例方法名称</th>");
            out.println("<th width=\"60px\">用例优先级</th>");
            out.println("<th width=\"100px\">操作/选择</th>");
            out.println("</tr>");
            while (rs.next()) {
                out.println("<tr id=\"row" + rownum + "\" height=\"50px\">");
                out.println("<th>");
                out.println(rs.getString("id"));
                out.println("</th>");
                out.println("<th>");
                out.println(rs.getString("interface_name"));
                out.println("</th>");
                out.println("<th>");
                out.println(rs.getString("scenario"));
                out.println("</th>");
                out.println("<th>");
                out.println(rs.getString("method"));
                out.println("</th>");
//                out.println("<th style=\"white-space: nowrap;overflow: hidden;text-overflow: ellipsis;width:20%\">");
//                out.println(rs.getString("expected_result"));
//                out.println("</th>");
//                out.println("<th style=\"white-space: nowrap;overflow: hidden;text-overflow: ellipsis;width:20%\">");
//                out.println(rs.getString("actual_result"));
//                out.println("</th>");
                out.println("<th style=\"word-wrap:break-word\";>");
                out.println(rs.getString("call_suff"));
                out.println("</th>");
                out.println("<th>");
                out.println(rs.getString("priority"));
                out.println("</th>");
                out.println("<th><a href=\"UpdataStep2.jsp?caseid=" + rs.getString("id") + "\" target=\"_Blank\">修改</a>|<input type=\"checkbox\" name=\"select\" value=\"" + rs.getString("id") + "\"/></th>");
                //out.println("<th>");
                //out.println(rs.getString("body"));
                //out.println("</th>");
                out.println("</tr>");
                rownum++;
            }
            out.println("</table>");
        } catch (SQLException se) {
            // 处理 JDBC 错误
            se.printStackTrace();
        } catch (Exception e) {
            // 处理 Class.forName 错误
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
            JDBCConnectionPool.close(rs, statement);
        }
    }

}
