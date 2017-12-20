package com.test;

import java.util.function.Supplier;
import java.util.stream.Stream;

public class FibonacciSupplier implements Supplier<Long> {
    long a = 0;
    long b = 1;

    @Override
    public Long get() {
        long x = a + b;
        a = b;
        b = x;
        return a;
    }

    public static void main(String[] args) {
        Stream<Long> fibonacci = Stream.generate(new FibonacciSupplier());
//        fibonacci.skip(20).limit(10).forEach(System.out::println);
//        List<Long> list = fibonacci.skip(20).limit(10).collect(toList());
        System.out.println(fibonacci.limit(10).reduce(0L,(a,b)->a+b));
    }
}
