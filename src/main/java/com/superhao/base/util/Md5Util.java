package com.superhao.base.util;


import org.springframework.util.DigestUtils;

import java.io.*;


/**
 * @Auther: zehao
 * @Date: 2019/4/22 22:11
 * @email: 928649522@qq.com
 * @Description:
 */
public class Md5Util {


    public static String getMD5(String str) {
        return DigestUtils.md5DigestAsHex(str.getBytes());
    }
    public static String getMD5(String str,String slat) {
        String base = str +"/"+slat;
        return DigestUtils.md5DigestAsHex(base.getBytes());
    }
    /**
     * 获取一个文件的md5值(可处理大文件)
     * @return md5 value
     */
    public static String getFileMD5(FileInputStream file) throws IOException {
            return DigestUtils.md5DigestAsHex(file);
    }
    /**
     * 获取一个文件的md5值(可处理大文件)
     * @return md5 value
     */
    public static String getFileMD5(InputStream file) throws IOException {
        return DigestUtils.md5DigestAsHex(file);
    }

    public static void main(String[] args) throws IOException {
        System.out.println(getFileMD5(null));
    }
}
