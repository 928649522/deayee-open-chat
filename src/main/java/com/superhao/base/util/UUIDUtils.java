package com.superhao.base.util;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

/**
 * @Auther: super
 * @Date: 2019/10/25 12:16
 * @email:
 */
public class UUIDUtils {

    public static String[] chars = new String[] { "a", "b", "c", "d", "e", "f",
            "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z" };

    private static Random random = new Random();

    private static String getUUID() {
        String id =null;
        UUID uuid = UUID.randomUUID();
        id = uuid.toString();

        //去掉随机ID的短横线
        id = id.replace("-", "");

        //将随机ID换成数字
        int num = id.hashCode();
        //去绝对值
        num = num < 0 ? -num : num;

        id = String.valueOf(num);

        return id;
    }
    public static String generateSixNumberUuid() {
       String res = "";
       for(int i =0;i<6;i++){
           res+=(random.nextInt(10));
       }
        return res;

    }



    public static void main(String[] args) {
        Set set =  new HashSet();
        for(int i =0;i<10000000;i++){
            set.add(generateNumberUuid());
        }
        System.out.println(set.size());
    }

    public static Long generateNumberUuid() {
        String target = getUUID()+getUUID();
        if(target.length()>20){
            return new Long("-"+target.substring(1,15));
        }
        return new Long("-"+target.substring(1,target.length()-4));
    }

    public static String generateShortUuid() {
        StringBuffer shortBuffer = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < 8; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 0x3E]);
        }
        return shortBuffer.toString();

    }
    public static String generateUuid() {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return uuid;

    }




}
