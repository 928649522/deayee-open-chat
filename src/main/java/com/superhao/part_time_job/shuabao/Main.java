package com.superhao.part_time_job.shuabao;

import java.util.Random;

/**
 * @Auther: zehao
 * @Date: 2019/6/10 13:27
 * @email: 928649522@qq.com
 */
public class Main {

    public static final int VIDEO_MAX_TIME=30;
    public static final String adbCommond="D:\\开发软件\\adb\\adb.exe shell input swipe 300 1000 300 500";
    public static void main(String[] args) throws InterruptedException {
        Random ra =new Random();

        int count=0;
        while(true){
                int sleepTime = VIDEO_MAX_TIME - ra.nextInt(20);
                if(sleepTime ==20){
                    sleepTime=0;
                }
            Thread.sleep(sleepTime*1000L);
            System.out.println("本次观片时间："+sleepTime+"s");
            count+=sleepTime;
            System.out.println("总共观看时间："+count+"s");
            CommandNoCmd.exeCmd(adbCommond);
        }

    }
}
