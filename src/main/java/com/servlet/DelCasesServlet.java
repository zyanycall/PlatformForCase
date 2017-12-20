package com.servlet;

import com.talk51.Utils.JDBCConnectionPool;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Created by zhaoyu on 2017/3/22.
 */
@WebServlet(value = "/batchDelCases")
public class DelCasesServlet extends HttpServlet {

    private String del_sql_cases = "DELETE from autotest.cases where id = ? ";
    private String del_sql_interface_list = "DELETE from autotest.interface_list where " +
            "interface_name = (SELECT interface_name from autotest.cases where id = ?) " +
            "and call_Stuff = (SELECT call_suff from autotest.cases where id = ?)";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String caseids = request.getParameter("preids");

        Connection connection = JDBCConnectionPool.getConnection();
        PreparedStatement pstmt_interface_list = null;
        PreparedStatement pstmt_cases = null;
        PrintWriter out = response.getWriter();

        int[] results_cases = new int[1];
        int[] results_interface_list = new int[1];
        try {
            //需要做事务
            connection.setAutoCommit(false);

            //interface_list数据作为附属数据，首先删除
            pstmt_interface_list = connection.prepareStatement(del_sql_interface_list);
            for (String caseId : caseids.split(",")) {
                pstmt_interface_list.setInt(1, Integer.parseInt(caseId));
                pstmt_interface_list.setInt(2, Integer.parseInt(caseId));
                pstmt_interface_list.addBatch();
            }
            results_interface_list = pstmt_interface_list.executeBatch();
            pstmt_cases = connection.prepareStatement(del_sql_cases);
            for (String caseId : caseids.split(",")) {
                pstmt_cases.setInt(1, Integer.parseInt(caseId));
                pstmt_cases.addBatch();
            }
            results_cases = pstmt_cases.executeBatch();

            connection.commit();
            for (int i : results_cases) {
                if (i < 1) {
                    out.write("error");
                    break;
                } else {
                    out.write("success");
                }
            }
            for (int i : results_interface_list) {
                if (i < 1) {
                    out.write("error");
                    break;
                } else {
                    out.write("success");
                }
            }
        } catch (Exception se) {
            JDBCConnectionPool.connectionRollback();
            out.write("error");
            se.printStackTrace();
        } finally {
            out.flush();
            out.close();
            JDBCConnectionPool.close(pstmt_interface_list);
            JDBCConnectionPool.close(pstmt_cases);
        }
    }
}