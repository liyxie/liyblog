package com.liy;

/**
 * @Author LiY
 */
public class test {
    public static void main(String[] args) {
        String i = "1234";
        Object i1 = "1234";
        Integer ii = Integer.parseInt(i);
        Integer ii1 = (Integer) i1;

        System.out.println(ii);
    }
}
