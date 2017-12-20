package com.servlet;

import com.talk51.Utils.StringTool;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringEscapeUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;


/**
 * Servlet implementation class JunitGen
 */
@WebServlet(name = "JunitGen", value = "/JunitGen")
public class JunitGen extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String model_name = request.getParameter("model_name");
        String interface_name = request.getParameter("interface_name");
        String interface_suff = request.getParameter("interface_suff");
        String host_ip = request.getParameter("host_ip");
        String priority = request.getParameter("priority");
        String expected_result = request.getParameter("expected_result");
        String scenario = request.getParameter("scenario");
        String method = request.getParameter("method");
        String body = StringEscapeUtils.escapeSql(URLDecoder.decode(request.getParameter("body"), "UTF-8"));
        String section2body = request.getParameter("section2body");
        String is_cache = request.getParameter("is_cache");

        PrintWriter out = response.getWriter();

        Configuration cfg = new Configuration();
        try {
            cfg.setDirectoryForTemplateLoading(new File("D:\\workplace\\PlatformForCase\\WebContent\\JunitTemplate"));
            Template template = cfg.getTemplate("JunitTemplates.ftl");
            Map map = new HashMap();
            map.put("Api_url", host_ip + interface_suff);

            Map parm_part = new HashMap();

            JSONObject temp = JSONObject.fromObject(section2body);
            for (Object k : temp.keySet()) {
                Object v = temp.get(k);
                parm_part.put(k.toString(), StringTool.toStingHex(v.toString()));
            }

            map.put("parmlength", parm_part.size());
            int i = 0;
            for (Object k : parm_part.keySet()) {
                map.put("parmkey" + i + "", k.toString());
                map.put("parmvalue" + i + "", parm_part.get(k));
                i++;
            }

            map.put("Method", method);
            template.process(map, out);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }

}
