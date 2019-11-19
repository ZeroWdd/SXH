package com.leyou.test;

import com.google.common.base.CaseFormat;
import org.junit.Test;

/**
 * @Auther: wdd
 * @Date: 2019/11/19 10:32
 * @Description:
 */
public class CaseFomat {
    @Test
    public void test(){
        System.out.println(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "admin_id"));//testData
        System.out.println(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "admin"));//testData
        System.out.println(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, "adminId"));//test_data
        System.out.println(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, "admin"));//test_data
    }
}
