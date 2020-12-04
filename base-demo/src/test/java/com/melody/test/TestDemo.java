package com.melody.test;  

/**
 * @description
 *
 * @author zqhuangc
 */
public class TestDemo {

    public static void main(String[] args) {
        int a = 2, b = 4;
        int c = 1<<a++*++b|6*2;
        a <<= b + 4 << 2 | 6 & 3;
        System.out.println(c + ": " + a + ": " +b);
        System.out.println(3 << (9 << 2 | 6 & 3));
    }
}
