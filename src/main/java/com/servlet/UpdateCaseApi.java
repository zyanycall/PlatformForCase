package com.servlet;

import com.talk51.Utils.JDBCConnectionPool;
import org.apache.commons.lang.StringEscapeUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Servlet implementation class UpdateCaseApi
 */
@WebServlet(name = "UpdateCaseApi", value = "/UpdateCaseApi")
public class UpdateCaseApi extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = response.getWriter();

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        HttpSession session = request.getSession();
        request.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        String model_name = request.getParameter("model_name");
        String interface_name = request.getParameter("interface_name");
        String interface_suff = request.getParameter("interface_suff");
        String host_ip = request.getParameter("host_ip");
        String priority = request.getParameter("priority");
        String expected_result = request.getParameter("expected_result");
        String scenario = request.getParameter("scenario");
        String method = request.getParameter("method");
        String body = StringEscapeUtils.escapeSql(URLDecoder.decode(request.getParameter("body"), "UTF-8"));
        String section2body = URLEncoder.encode(request.getParameter("section2body"), "UTF-8");
        String is_cache = request.getParameter("is_cache");
        String updataid = request.getParameter("updataid");

        int user_id = 0;
        try {
            user_id = (Integer) session.getAttribute("userid");
        } catch (RuntimeException e) {
            user_id = 0;
        }

        Connection connection = JDBCConnectionPool.getConnection();
        Statement statement = null;
        ResultSet rss2pre = null;
        try {
            statement = connection.createStatement();
            String updata_sql = "update cases set project_name='" + model_name + "',interface_name='" + interface_name + "',call_suff='" + interface_suff + "',host_ip='" + host_ip + "',priority=" + priority + ",expected_result='" + expected_result + "',scenario='" + scenario + "',method='" + method + "',body='" + body +
                    "',operator=" + user_id + ",is_cache=" + is_cache + " where id=" + updataid + "";
            statement.executeUpdate(updata_sql);

            String s2preupdata_sql = "select * from cases_parm_relation where case_id=" + updataid + "";
            rss2pre = statement.executeQuery(s2preupdata_sql);
            if (!rss2pre.next()) {
                String new_s2inert = "insert into cases_parm_relation (case_id,parm_relation,is_deleted) values (" + updataid + ",'" + section2body + "',0)";
                statement.executeUpdate(new_s2inert);
            }

            String s2updata_sql = "update cases_parm_relation set parm_relation='" + section2body + "' where case_id=" + updataid + "";
            statement.executeUpdate(s2updata_sql);

            String check_sql = "select * from interface_list where call_Stuff = '" + interface_suff + "' and interface_name = '" + interface_name + "' and model_name='" + model_name + "'";
            rss2pre = statement.executeQuery(check_sql);

            if (!rss2pre.next()) {
                String new_insert_interface_list_sql = "insert into interface_list (model_name,interface_name,call_Stuff) values ('" + model_name + "','" + interface_name + "','" + interface_suff + "')";
                statement.executeUpdate(new_insert_interface_list_sql);
            }

            statement.close();
            out.print("success");
        } catch (SQLException e) {
            e.printStackTrace();
            out.print("error");
        } finally {
            out.flush();
            out.close();
            JDBCConnectionPool.close(rss2pre, statement);
        }
    }
}