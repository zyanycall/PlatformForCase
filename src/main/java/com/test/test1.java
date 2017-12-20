package com.test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class test1 {
    public static void main(String[] args) {
//        arrangementSelect(new String[] {
//                "1", "2", "3", "4"
//        }, 2);
//        for (int i=1 ; i<10; i++) {
//            combinationSelect(new String[] {
//                    "44294","38867","38868","350010071","48200","350010033","106499","43875","350000407","350010060","350010027","1786","4436","350000408","350010048","350010030","2111","1874","350010032","350000409","350000420","0","1719","3500897","2248","370001041","350000404","350010035","1836","350010031","3349","350000418","350010051","1592","2274","3500816","350000423","350010028","2841","1510","350000424","33611","34121","19238","350010005","2525"
//            }, i);
//        }
        combinationSelect(new String[]{
                "236666","77","3500800","3500816","3500873","3500919","3500874","3500778","3500776","3500876","3500897","3500820","350000404","3500959","3500736","3500749","7079","87","3079","350000422","350000420","350000407","350000430","350000413","350000423","350000408","350000409","350000897","40719","5555","350000415","213","27713","20000","213923","8802","108001","108002","2229932","100","6666","108003","179000","3501033","350000411","350000412","350000424","350000414","350000417","350000373","1000","350000416","90","33567","89","350000475","350000476","350000468","350000434","350000473","2147483647","350000999","350000467","350000471","350000433","8899","404","44294","38867","38868","350010029","350010071","106499","43875","350010060","1786","18511","4436","350010095","370001043","350010028","370001041","48200","350010027","1874","1719","1592","19238","33289","350000429","370001039","350010096","36694","34269","35695","350010030","2525","3349","1510","350000418","2248","2274","350010074","2059","2841","3948","350010017","350010051","2213","350010104","2835","1836","2497","2111","48201","0","107127","798798","798799","34555","6677","2717","848960","1348960","1848960","998832","700761","998833","1200761","1700761","350010007","350010005","33640","2936","3094","2910","3819","4039","4123","4431","4492","4388","4260","4184","4183","3933","4046","3496","4222","3787","4192","3822","4113","4500","4189","4547","4495","4665","4259","4218","4628","3466","3863","3920","3971","24443344","72893984","834398944","9993434","34121","3784","4169","4161","4408","4491","4095","4020","4400","4445","4421","4454","4446","4401","350010008","350010702","3916","350010019","3892","350010023","4049","350010025","4023","350010026","4174","350010031","4120","350010034","350010035","4170","4173","350010040","3861","350010045","4085","4627","350010062","350010064","4617","350010067","4565","350010072","350010012","4675","350010078","350010079","4709","4684","350010081","4656","350010082","4616","4817","4861","4874","350010105","350010107","4909","4941","350010108","350010109","38174","4677","350010073","350010110","4655","4216","4680","4865","4757","4818","4755","4910","4904","350010112","4902","4893","4916","4997","4972","4242","41644","4711","4596","4613","350010111","4223","4652","4548","4911","4851","4550","350010129","4962","4890","4923","350010241","4574","4701","4687","43849","350010113","350010116","350010021","350010118","370001044","350010048","36397","38374","40206","350010024","350010011","1239115","889911","739115","1739115","90933603","97554788","80476629","98006082","816066","1316066","1816066","36399","350010033"
        }, 7);

    }

    /**
     * 排列选择（从列表中选择n个排列）
     *
     * @param dataList 待选列表
     * @param n        选择个数
     */
    public static void arrangementSelect(String[] dataList, int n) {
        System.out.println(String.format("A(%d, %d) = %d", dataList.length, n, arrangement(dataList.length, n)));
        arrangementSelect(dataList, new String[n], 0);
    }

    /**
     * 排列选择
     *
     * @param dataList    待选列表
     * @param resultList  前面（resultIndex-1）个的排列结果
     * @param resultIndex 选择索引，从0开始
     */
    private static void arrangementSelect(String[] dataList, String[] resultList, int resultIndex) {
        int resultLen = resultList.length;
        if (resultIndex >= resultLen) { // 全部选择完时，输出排列结果
            System.out.println(Arrays.asList(resultList));
            return;
        }

        // 递归选择下一个
        for (int i = 0; i < dataList.length; i++) {
            // 判断待选项是否存在于排列结果中
            boolean exists = false;
            for (int j = 0; j < resultIndex; j++) {
                if (dataList[i].equals(resultList[j])) {
                    exists = true;
                    break;
                }
            }
            if (!exists) { // 排列结果不存在该项，才可选择
                resultList[resultIndex] = dataList[i];
                arrangementSelect(dataList, resultList, resultIndex + 1);
            }
        }
    }

    /**
     * 组合选择（从列表中选择n个组合）
     *
     * @param dataList 待选列表
     * @param n        选择个数
     */
    public static void combinationSelect(String[] dataList, int n) {
//        System.out.println(String.format("C(%d, %d) = %d", dataList.length, n, combination(dataList.length, n)));
        combinationSelect(dataList, 0, new String[n], 0);
    }

    /**
     * 组合选择
     *
     * @param dataList    待选列表
     * @param dataIndex   待选开始索引
     * @param resultList  前面（resultIndex-1）个的组合结果
     * @param resultIndex 选择索引，从0开始
     */
    private static void combinationSelect(String[] dataList, int dataIndex, String[] resultList, int resultIndex) {
        int resultLen = resultList.length;
        int resultCount = resultIndex + 1;
        if (resultCount > resultLen) { // 全部选择完时，输出组合结果
//            System.out.println(Arrays.asList(resultList).toString().replaceAll("\\[","").replaceAll("]", ""));
            writeFile("E:\\auto_Java_Interface_zhaoyu\\tea_id.txt", Arrays.asList(resultList).toString().replaceAll("\\[", "").replaceAll("]", "").replaceAll(" ", "") + "\n");
            return;
        }

        // 递归选择下一个
        for (int i = dataIndex; i < dataList.length + resultCount - resultLen; i++) {
            resultList[resultIndex] = dataList[i];
            combinationSelect(dataList, i + 1, resultList, resultIndex + 1);
        }
    }

    /**
     * 计算阶乘数，即n! = n * (n-1) * ... * 2 * 1
     *
     * @param n
     * @return
     */
    public static long factorial(int n) {
        return (n > 1) ? n * factorial(n - 1) : 1;
    }

    /**
     * 计算排列数，即A(n, m) = n!/(n-m)!
     *
     * @param n
     * @param m
     * @return
     */
    public static long arrangement(int n, int m) {
        return (n >= m) ? factorial(n) / factorial(n - m) : 0;
    }

    /**
     * 计算组合数，即C(n, m) = n!/((n-m)! * m!)
     *
     * @param n
     * @param m
     * @return
     */
    public static long combination(int n, int m) {
        return (n >= m) ? factorial(n) / factorial(n - m) / factorial(m) : 0;
    }

    /**
     * 追加文件：使用FileWriter
     *
     * @param fileName
     * @param content
     */
    public static void writeFile(String fileName, String content) {
        try {
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            FileWriter writer = new FileWriter(fileName, true);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
