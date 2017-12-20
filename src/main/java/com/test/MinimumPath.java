package com.test;

/**
 *   有一个矩阵map，它每个格子有一个权值。从左上角的格子开始每次只能向右或者向下走，最后到达右下角的位置，路径上所有的数字累加起来就是路径和，返回所有的路径中最小的路径和。
 给定一个矩阵map及它的行数n和列数m，请返回最小路径和。保证行列数均小于等于100.
 测试样例：
 [[1,2,3],[1,1,1]],2,3
 返回：4
 解析：设dp[n][m]为走到n*m位置的路径长度，那么显而易见dp[n][m] = min(dp[n-1][m],dp[n][m-1]);
 */
public class MinimumPath {

    public static int getMin(int[][] map, int n, int m) {
        // write code here
        // dp用于记录外围行列的到达的成本，如本例题，外围的成本如下
        // 1 3 6
        // 2 0 0
        // 由于下面的计算方式，会将第一个格变成2，这不重要，因为它不参与动态规划的最终计算
        int[][] dp = new int[n][m];
        // 算出列的成本
        // 1
        // 2
        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= i; j++) {
                dp[i][0] += map[j][0];
            }
        }
        // 算出行的成本
        // 2 3 6
        // 2 0 0
        for (int i = 0; i < m; i++) {
            for (int j = 0; j <= i; j++) {
                dp[0][i] += map[0][j];
            }
        }
        // 将外围的成本和原始数据进行计算。
        // i j 用1为起点，是为了不计算外围已经计算好的值
        // 未计算的值，都是运用之前注释分析的方法，min方法。
        for (int i = 1; i < n; i++) {
            for (int j = 1; j < m; j++) {
                dp[i][j] = min(dp[i][j - 1] + map[i][j], dp[i - 1][j] + map[i][j]);
            }
        }
        // dp被运算完后，就是整个成本的二维数组，而dp[n-1][m-1]就是要求的值，dp[n][m]不存在，因为下标从0开始
        return dp[n - 1][m - 1];
    }

    public static int min(int a, int b) {
        return a > b ? b : a;
    }

    public static void main(String[] args) {
        int [][]a = {{1,2,3},{1,1,1}};

        System.out.println(getMin(a,2,3));
    }

}
