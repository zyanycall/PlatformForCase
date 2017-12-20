package com.talk51.courseScheduling;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * 排课自动化测试用例
 * Created by zhaoyu on 2017/5/23.
 */
public class TestCourseScheduling {

    /**
     * 先做用例校验，还没有大批量的数据导入
     * {"result":[{"tea_type":1,"candidate":[]},{"tea_type":2,"candidate":[]},{"tea_type":2,"candidate":[]},{"tea_type":2,"candidate":[]}],"class_id":"1","end_stamp":"20170902 07:59:59","start_stamp":"20170610 00:00:00","level":"470","error":"","class_type":"","state":"succ","teacher_group":"5,6,7","bu":"32122"}
     */
    private static String uri1 = "http://172.16.0.241:17416/RecommendRoomsServer?teacher_group=5,6,7&level=470&class_id=1&start_time=1496682061&end_time=1504310399&bu=32122";
    private static JSONObject resultJsonObj = null;
    List<NameValuePair> queryNamePair = null;

    // 减少sql调用的次数，内存中存一份老师的集合，减少检查的时间。
    private static HashMap<String, Teacher> teacherMap = new HashMap<String, Teacher>();

    @Before
    public void before() throws IOException {
        //直接执行
        HttpUtil.httpGet(uri1);
        //测试是否有返回(失败一般为请求IP错误)
        Assert.assertNotNull(HttpUtil.getResult());
        resultJsonObj = JSONObject.fromObject(HttpUtil.getResult());
        queryNamePair = URLEncodedUtils.parse(uri1, Charset.forName("UTF-8"));
    }

    /**
     * 测试是否有返回具体内容
     */
    @Test
    public void testContent() {
        // 解析返回的Json
        String state = resultJsonObj.getString(Const.KEY_STATE);
        JSONArray teacherJsonArray = resultJsonObj.getJSONArray(Const.KEY_RESULT);
        Assert.assertEquals(Const.SUCCESS, state);
        Assert.assertNotNull(teacherJsonArray);
    }

    /**
     * 测试返回时间，是星期五六日就好。
     * 本身start_stamp时间没有实际意义，是返回值
     */
    @Test
    public void testStartEndStamp() throws ParseException {
        // 解析返回的Json
        String startStamp = resultJsonObj.getString(Const.KEY_START_STAMP);
        String endStamp = resultJsonObj.getString(Const.KEY_END_STAMP);
        if (checkData567(startStamp) || checkData567(endStamp)) {
            Assert.fail();
        }

    }

    /**
     * 测试推荐老师的数量为4个：1个中教，3个外教
     */
    @Test
    public void testTeacherCount() {
        JSONArray teacherJsonArray = resultJsonObj.getJSONArray(Const.KEY_RESULT);
        Assert.assertEquals(4, teacherJsonArray.size());
        int chTeacherCount = 0;
        int enTeacherCount = 0;
        for (int i = 0; i < teacherJsonArray.size(); i++) {
            JSONObject jsonObj = teacherJsonArray.getJSONObject(i);
            if (Const.CH_TEACHER_TYPE == jsonObj.getInt(Const.KEY_TEA_TYPE)) {
                chTeacherCount++;
            } else if (Const.EN_TEACHER_TYPE == jsonObj.getInt(Const.KEY_TEA_TYPE)) {
                enTeacherCount++;
            } else {
                // 出现了teacherType不是中教也不是外教的数据
                Assert.fail();
            }
        }
        Assert.assertEquals(1, chTeacherCount);
        Assert.assertEquals(3, enTeacherCount);
    }

    /**
     * 校验老师的数据正确性
     */
    @Test
    public void testTeacherData() {
        JSONArray resultJsonArray = resultJsonObj.getJSONArray(Const.KEY_RESULT);
        int tea_type = 0;
        // 遍历room集合
        for (int i = 0; i < resultJsonArray.size(); i++) {
            // 3个外教1个中教之一的room
            JSONObject roomJsonObj = resultJsonArray.getJSONObject(i);
            // 得到老师类型，老师类型1中教；2外教
            tea_type = roomJsonObj.getInt(Const.KEY_TEA_TYPE);
            // 候选人的集合
            JSONArray candidateJsonArray = roomJsonObj.getJSONArray(Const.KEY_CANDIDATE);
            // 遍历候选人集合
            for (int j = 0; j < candidateJsonArray.size(); j++) {
                // 得到time时间点
                JSONObject candidateJsonObj = candidateJsonArray.getJSONObject(j);
                // 判断时间点 time 是否符合要求：
                // 中教老师的上课时间：周六日的早上7点到12点，晚上5点到10点（课程结束时间）
                // 外教老师上课时间：周一到周五，晚上5点到10点（课程结束时间）
                checkTeacherTime(candidateJsonObj, tea_type);

                // 得到time时间点下的老师集合，这是真正要判断的。
                JSONArray teachersJsonArray = candidateJsonObj.getJSONArray(Const.KEY_TEACHERS);
                for (int k = 0; k < teachersJsonArray.size(); k++) {
                    JSONObject teaJsonObj = teachersJsonArray.getJSONObject(k);
                    //开始判断teacher对象里的数据正确性
                    checkTeacherData(teaJsonObj, queryNamePair, tea_type);
                }
            }
        }
    }


    /**
     * 1.数据本身是从表中查得到的，且是准确的（应该是肯定的）
     * 校验老师的数据，是否都来自数据库
     */
    private void checkTeacherData(JSONObject teaJsonObj, List<NameValuePair> queryNamePair, int tea_type) {
        // 将teacher 的JSON对象转换成本地Teacher对象
        // 根据 tea_id 获取teacher数据库中的数据，并转换成teacher对象。
        // 对比两个teacher对象是否相等（重写equal方法）
        // Json对象是返回值读取，可能每次读取的并不同，但从数据库中读取，都是一样的。
        Teacher teaFromJson = new Teacher(teaJsonObj, tea_type);
        Teacher teaFromMySql;
        String tea_id = teaJsonObj.getString(Const.KEY_TEA_ID);
        if (teacherMap.containsKey(tea_id)) {
            teaFromMySql = teacherMap.get(tea_id);
        } else {
            teaFromMySql = new Teacher(tea_id, queryNamePair, tea_type);
            teacherMap.put(tea_id, teaFromMySql);
        }

        if (!teaFromJson.equals(teaFromMySql)) {
            System.out.println("JSON 转的对象：\n" + teaFromJson.toString());
            System.out.println("MySql 转的对象：\n" + teaFromMySql.toString());
            Assert.fail();
        }
        System.out.println(teaJsonObj.getString(Const.KEY_TEA_NAME));
    }

    /**
     * 中教老师的上课时间：周六日的早上7点到12点，晚上5点到10点（课程结束时间）
     * 外教老师上课时间：周一到周五，晚上5点到10点（课程结束时间）
     */
    private void checkTeacherTime(JSONObject candidateJsonObj, int tea_type) {
        // timeJsonObj 中包含多个时间点，每个时间点中需要对星期几 + 时间判断
        JSONArray timesJsonArray = candidateJsonObj.getJSONArray(Const.KEY_TIMES);
        for (int i = 0; i < timesJsonArray.size(); i++) {
            JSONObject time = timesJsonArray.getJSONObject(i);
            // 对星期校验
            int dayOfWeek = time.getInt(Const.KEY_DAY_OF_WEEK);
            if (tea_type == Const.CH_TEACHER_TYPE) { // 中教，必须是周六日
                if (dayOfWeek != Const.SATURDAY && dayOfWeek != Const.SUNDAY) {
                    Assert.fail();
                }
            } else { // 外教，必须是周一到周五
                if (dayOfWeek <= 0 && dayOfWeek >= Const.SATURDAY) {
                    Assert.fail();
                }
            }

            // 对时间校验
            String start_time = time.getString(Const.KEY_START_TIME);
            String end_time = time.getString(Const.KEY_END_TIME);
            int start_time_int = Integer.parseInt(start_time.replaceAll(":", ""));
            int end_time_int = Integer.parseInt(end_time.replaceAll(":", ""));
            // 1.时间间隔小于一小时
            if (end_time_int - start_time_int > 60) {
                Assert.fail();
            }
            // 2.符合指定的时间:中教7-12 + 17-22，外教17-22
            if (tea_type == Const.CH_TEACHER_TYPE) {
                if (start_time_int < 0700 || end_time_int > 2200) {
                    Assert.fail();
                }
                if ((start_time_int > 1200 && start_time_int < 1700) || (end_time_int > 1200 && end_time_int < 1700)) {
                    Assert.fail();
                }
            } else { // 外教
                if (start_time_int < 1700 || end_time_int > 2200) {
                    Assert.fail();
                }
            }
        }
    }

    /**
     * 判断是否是星期五六日
     */
    private boolean checkData567(String dateStr) throws ParseException {
        int dayForWeek = getDayForWeek(dateStr);
        // 判断是否是5,6,7之一
        switch (dayForWeek) {
            case Const.FRIDAY:
                return false;
            case Const.SATURDAY:
                return false;
            case Const.SUNDAY:
                return false;
            default:
                return true;
        }
    }

    /**
     * 获取星期几，星期日为7
     *
     * @param dateStr
     * @return
     * @throws ParseException
     */
    private static int getDayForWeek(String dateStr) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(sdf.parse(dateStr));
        int dayForWeek = 0;
        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            dayForWeek = Const.SUNDAY;
        } else {
            dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        }
        return dayForWeek;
    }
}