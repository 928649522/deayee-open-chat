package com.superhao.part_time_job.shuabao;

/**
 * @Auther: zehao
 * @Date: 2019/6/10 13:28
 * @email: 928649522@qq.com
 */
public class OperateHandle  implements Runnable{
    private String adbCommand;

    public OperateHandle(String adbCommand){
        this.adbCommand = adbCommand;
    }
    @Override
    public void run() {
        CommandNoCmd.exeCmd(adbCommand);
    }
}
