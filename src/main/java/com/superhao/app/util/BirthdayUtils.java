package com.superhao.app.util;

import java.util.Calendar;
import java.util.Date;

/**
 * @Auther: super
 * @Date: 2019/11/12 14:05
 * @email:
 */
public class BirthdayUtils {
    public static int getAgeByBirthday(Date birthday) {
        if(birthday==null){
            return 0;
        }
        Calendar cal = Calendar.getInstance();
        if (cal.before(birthday)) {
            throw new IllegalArgumentException(
                    "The birthDay is before Now.It's unbelievable!");
        }
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH) + 1;
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(birthday);
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH) + 1;
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
        int age = yearNow - yearBirth;
        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
// monthNow==monthBirth 
                if (dayOfMonthNow < dayOfMonthBirth) {
                    age--;
                }
            } else {
// monthNow>monthBirth 
                age--;
            }
        }
        return age;
    }


}
