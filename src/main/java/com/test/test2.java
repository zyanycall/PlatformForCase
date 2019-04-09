package com.test;

public class test2 {

    public static double getM(double bb, int n, double lixi) {

        if (n == 0) {
            return 0;
        }
        if (n == 1) {
            return Math.pow(lixi, n)* bb;
        }
        return Math.pow(lixi, n)* bb + getM(bb, n - 1, lixi);
    }

    public static void main(String[] args) {
        double number = 50;
        int count = 31;
        double lixi = 1.05d;

        for (int i = 1 ; i < count; i ++) {
            System.out.println(getM(number, i, lixi));
        }

//        System.out.println(3.12 * Math.pow(1.05, 20));
        System.out.println("==============================");

        for (int i = 1 ; i < count; i ++) {
            System.out.println(number * Math.pow(lixi, i));
        }

        System.out.println("==============================");

        for (int i = 1 ; i < count; i ++) {
            System.out.println(number * i);
        }

    }


}
