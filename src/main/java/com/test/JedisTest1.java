package com.test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Map;
import java.util.Set;

public class JedisTest1 {

    public static void redisPrintZrangeValue(Jedis jedis, String key){
        Set zList = jedis.keys(key);
        Object[] listArray = zList.toArray();
        if (listArray.length>0) {
            System.out.println(jedis.zrange(key, 0, -1).toString());
        }

    }

    public static void redisPrintHgetValue(Jedis jedis, String preFixKey){
        Set zList = jedis.keys(preFixKey);
        Object[] listArray = zList.toArray();
        for (int i=0; i<listArray.length; i++) {
            Map map = jedis.hgetAll(listArray[i].toString());
            for(Object entry: map.entrySet()) {
//                vars.put("6379 :  " + entry.getKey().toString(), "=" + entry.getValue().toString());
                Map.Entry entiry1 = (Map.Entry)entry;
                System.out.println(entiry1.getKey().toString());
                System.out.println(entiry1.getValue().toString());
            }
            //vars.put("6379 : "+listArray[i], jedis.hgetAll(listArray[i]).toString());
        }
    }

    public static void main(String[] args) {

        String redisHost = "172.16.0.20";

        JedisPoolConfig config = new JedisPoolConfig();

        JedisPool pool = new JedisPool(config, redisHost, 6379, 2000);
        Jedis jedis = null;
        jedis = pool.getResource();

        String c_cate_id1 = "39755073";
        String c_cate_id2 = "96083690";
        String level_id = "587418";
        String c_occup_id = "766";
        String occup_people = "785";

        String key1 = "course_redis_course_6_"+c_cate_id1+"_"+c_cate_id2;
        String key2 = "course_redis_course_6_"+c_cate_id1+"_"+c_cate_id2+"_"+level_id+"_"+c_occup_id+"_"+occup_people;
        String key3 = "course_redis_course_6_"+c_cate_id1+"_"+c_cate_id2+"_"+level_id+"_"+c_occup_id+"_"+occup_people+"_endTime";

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        redisPrintZrangeValue(jedis, key1);
//        redisPrintZrangeValue(jedis,key2);
//        redisPrintZrangeValue(jedis,key3);

        redisPrintHgetValue(jedis,"java_appoint_1vn_consumer_stu_timetable_567678");


        jedis.quit();
        pool.close();
    }
}
