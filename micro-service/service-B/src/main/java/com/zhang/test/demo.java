package com.zhang.test;

import java.sql.Array;
import java.util.*;


/**
 * @Author: Mr.ZHANG
 * @Date: 2019/1/23 23 上午 10:39
 */
public class demo {

    public static void main(String[] args) {
        int a = 1;
        int b = 2;
        double c = 3;
        double d = 2;

//        System.out.println(a / b);
//        System.out.println(c / d);
//        System.out.println(UUID.randomUUID().toString().replace("-","").toUpperCase());
//        System.out.println(+a);
//        System.out.println(-a);


        List<String> list = Arrays.asList("1111","2222","2222222");
        for (String s : list) {
            System.out.println(s);
        }

        list.stream().filter(e -> e.length() < 5).forEachOrdered(System.out::println);


    }


}