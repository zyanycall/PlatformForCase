package com.test;

/**
 * 有数组penny，penny中所有的值都为正数且不重复。每个值代表一种面值的货币，每种面值的货币可以使用任意张，再给定一个整数aim(小于等于1000)代表要找的钱数，求换钱有多少种方法。
 给定数组penny及它的大小(小于等于50)，同时给定一个整数aim，请返回有多少种方法可以凑成aim。
 测试样例：
 [1,2,4],3,3
 返回：2
 解析：设dp[n][m]为使用前n中货币凑成的m的种数，那么就会有两种情况：
 使用第n种货币：dp[n-1][m]+dp[n-1][m-peney[n]]
 不用第n种货币：dp[n-1][m]，为什么不使用第n种货币呢，因为penney[n]>m。
 这样就可以求出当m>=penney[n]时 dp[n][m] = dp[n-1][m]+dp[n-1][m-peney[n]]，否则，dp[n][m] = dp[n-1][m]
 */
public class Exchange {
    public static int countWays(int[] penny, int n, int aim) {
        // write code here
        // 异常数据判断
        if(n==0||penny==null||aim<0){
            return 0;
        }
        // 初始化二维数组
        int[][] pd = new int[n][aim+1];
        // 初始化数据，因为aim>0,如果aim为0可以理解为都取0也是1种情况
        for(int i=0;i<n;i++){
            pd[i][0] = 1;
        }
        // 初始化数据，主要是初始化起点的数据，即如果第二个钱比aim小，才会有效，初始化仍然是1
        for(int i=1;penny[0]*i<=aim;i++){
            pd[0][penny[0]*i] = 1;
        }
        // 并非是递归，是动态规划，即按顺序从小到大计算出值，最后计算出的即结果。
        for(int i=1;i<n;i++){
            for(int j=0;j<=aim;j++){
                // 一个一个来的，从小到大，公式是前面注释提到的。
                if(j>=penny[i]){
                    pd[i][j] = pd[i-1][j]+pd[i][j-penny[i]];
                }else{
                    pd[i][j] = pd[i-1][j];
                }
            }
        }
        return pd[n-1][aim];
    }

    public static void main(String[] args) {
        int []a = {4,2,1};
        System.out.println(countWays(a,3,0));
    }
}
