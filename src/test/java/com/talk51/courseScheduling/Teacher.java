package com.talk51.courseScheduling;

import net.sf.json.JSONObject;
import org.apache.http.NameValuePair;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoyu on 2017/5/23.
 */
public class Teacher {

    private String tea_id = "";
    private String start_date = "";
    private String end_date = "";
    private String tea_name = "";
    private List<String> tea_level_list = new ArrayList<String>();
    private String sub_cate = "";
    private String tea_score = "";
    private String time_full = "";
    private String tea_group = "";
    private int tea_type = 0;

    // 默认构造方法
    Teacher(){
        super();
    }

    // JSON对象中获取teacher的对象，中外教结构相同，故使用同一个构造函数
    Teacher(JSONObject teacher, int tea_type) {
        this.tea_level_list.add(teacher.getString(Const.KEY_TEA_LEVEL));
        this.tea_name = teacher.getString(Const.KEY_TEA_NAME);
        this.start_date = teacher.getString(Const.KEY_START_DATE);
        this.end_date = teacher.getString(Const.KEY_END_DATE);
        this.tea_id = teacher.getString(Const.KEY_TEA_ID);
        this.tea_type = tea_type;
    }

    // 通过老师唯一标示id获取teacher对象，中外教表数据不同。
    Teacher(String tea_id, List<NameValuePair> queryNamePair, int tea_type) {
        this.tea_type = tea_type;
        this.tea_id = tea_id;
        // 获取老师的level，中教外教都是
        this.tea_level_list = getTeacherLevel(tea_id, queryNamePair, tea_type);

        // 中教
        if (tea_type == Const.CH_TEACHER_TYPE) {
            // 获取老师名称 和 bu
            JDBCConnectionPool chNameConPool = new JDBCConnectionPool("jdbc:mysql://172.16.0.20:3306/multi_class?user=root&password=123456");
            Connection chNameCon = chNameConPool.getConnection();
            Statement chNameStatement = null;
            ResultSet chNameRs = null;
            try {
                chNameStatement = chNameCon.createStatement();
                String chTeacherNameQuery = "SELECT real_name, subcategory FROM `mc_teacher` where sso_id = " + tea_id + ";";
                chNameRs = chNameStatement.executeQuery(chTeacherNameQuery);
                while (chNameRs.next()) {
                    this.tea_name = chNameRs.getString("real_name");
                    this.sub_cate = chNameRs.getString("subcategory");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                chNameConPool.close(chNameRs, chNameStatement);
            }
        } else { // 外教，获取老师名称  和  外教老师分组
            JDBCConnectionPool enNameConPool = new JDBCConnectionPool("jdbc:mysql://172.16.0.176:3306/talkplatform_teacher?user=root&password=51talk.com");
            Connection enNameCon = enNameConPool.getConnection();
            Statement enNameStatement = null;
            ResultSet enNameRs = null;
            try {
                enNameStatement = enNameCon.createStatement();
                String enTeacherNameQuery = "SELECT real_name, is_full_time FROM `teacher` where id = " + tea_id + ";";
                enNameRs = enNameStatement.executeQuery(enTeacherNameQuery);
                while (enNameRs.next()) {
                    this.tea_name = enNameRs.getString("real_name");
                    this.tea_group = enNameRs.getString("is_full_time");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                enNameConPool.close(enNameRs, enNameStatement);
            }
        }
    }


    /**
     * 通过老师id，从数据库表中查询出老师的level，如果找不到，则返回默认老师level
     */
    private List<String> getTeacherLevel(String tea_id, List<NameValuePair> queryNamePair, int tea_type) {
        ArrayList<String> levelList = new ArrayList<String>();
        // 获取老师level，需要传入参数
        JDBCConnectionPool chLevelConPool = new JDBCConnectionPool("jdbc:mysql://172.16.0.20:3306/talkplatform_course?user=root&password=123456");
        Connection chLevelCon = chLevelConPool.getConnection();
        Statement chLevelStatement = null;
        ResultSet chLevelRs = null;
        try {
            String chBu = "";
            // 获取参数所传的BU
            for (NameValuePair pair : queryNamePair) {
                if (pair.getName().equals(Const.BU)) {
                    chBu = pair.getValue();
                }
            }
            chLevelStatement = chLevelCon.createStatement();
            // 获取level的sql
            String teacher_level_query = "SELECT level_id FROM `base_course_class_rule` where class_id in (select class_id from `base_course_class_room_teacher` where tea_id =" + tea_id + " ); ";
            chLevelRs = chLevelStatement.executeQuery(teacher_level_query);
            while (chLevelRs.next()) {
                String level_it = chLevelRs.getString("level_id");
                levelList.add(level_it);
            }
            if (levelList.isEmpty()) {
                levelList.add(new String(Const.DEFAULT_LEVEL_INT + ""));
            }
            return levelList;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            chLevelConPool.close(chLevelRs, chLevelStatement);
        }
        return levelList;
    }


    /**
     * 重写hashcode
     */
    public int hashCode() {
        int result = 17;
        result = 37 * result + Integer.parseInt(tea_id);
        result = 37 * result + tea_type;
        result = 37 * result + end_date.hashCode();
        result = 37 * result + start_date.hashCode();
        result = 37 * result + tea_name.hashCode();
        result = 37 * result + levelsHashCode(tea_level_list);
        result = 37 * result + sub_cate.hashCode();
        result = 37 * result + tea_score.hashCode();
        result = 37 * result + time_full.hashCode();
        result = 37 * result + tea_group.hashCode();
        return result;
    }

    /**
     * 计算老师level数组的result，用于计算hashcode
     */
    private int levelsHashCode(List<String> tea_level_list) {
        int result = 17;
        for (String level : tea_level_list)
            result = 37 * result + level.hashCode();
        return result;
    }

    /**
     * 重写equals，并不是所有字段都比较
     * 关键是tea_level_list
     */
    public boolean equals(Object o) {
        if (!(o instanceof Teacher)) {
            return false;
        }
        Teacher t = (Teacher) o;
        return tea_id.equals(t.tea_id)
                && tea_name.equals(t.tea_name)
                && tea_type == t.tea_type
                && levelsEquals(t);
//                && start_date.equals(t.start_date)
//                && end_date.equals(t.end_date)
    }

    /**
     * 老师之间level有交集，则认为level正确
     * 由于老师的level比较少，则直接判断比较了，注意是比较的等号左端是外循环
     */
    private boolean levelsEquals(Teacher t) {
        for (String level : this.tea_level_list) {
            if (t.tea_level_list.contains(level)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 重写toString方法，打印关键数据。
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("tea_id 是 ：").append(tea_id).append("\n")
                .append("tea_name 是：").append(tea_name).append("\n")
                .append("tea_type 是：").append(tea_type).append("\n")
                .append("tea_level 包括：");
        for (String level : tea_level_list) {
            sb.append(level).append("   ");
        }
        return sb.toString();
    }
}
