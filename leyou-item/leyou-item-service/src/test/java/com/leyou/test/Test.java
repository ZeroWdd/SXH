package com.leyou.test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Auther: wdd
 * @Date: 2019/10/25 11:44
 * @Description:
 */
public class Test {

    @org.junit.Test
    public void test(){
        List<String> strings = Arrays.asList("abc", "", "bc", "efg", "abcd","", "jkl");
        List<String> filtered = strings.stream().filter(string -> !string.isEmpty()).collect(Collectors.toList());
        for (String s : filtered) {
            System.out.println(s);
        }
    }

}
