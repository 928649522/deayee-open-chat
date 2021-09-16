package com.superhao.part_time_job.decode;

import java.util.Base64;

/**
 * @Auther: zehao
 * @Date: 2019/10/2 17:01
 * @email: 928649522@qq.com
 */
public class Test1 {

    public static void main(String[] args) {
        String str = "5LiK5rW35rOV5ouJ5Yip5L+x5LmQ6YOoCuS4iuWIhi80NzAg5bey5aSE55CG77yM6K+35p+l5pS277yB";
        System.out.println(Base64Utils.decodeBase64(str));


    }
}
