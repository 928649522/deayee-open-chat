package com.superhao.part_time_job.entity;

/**
 * @Auther: zehao
 * @Date: 2019/10/2 22:28
 * @email: 928649522@qq.com
 */
public class BC1 {
    private String username;
    private String password;
    private String yzm_code;
    private String verifycode;
    private boolean remember;

    public BC1(){
        this.remember = false;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getYzm_code() {
        return yzm_code;
    }

    public void setYzm_code(String yzm_code) {
        this.yzm_code = yzm_code;
    }

    public String getVerifycode() {
        return verifycode;
    }

    public void setVerifycode(String verifycode) {
        this.verifycode = verifycode;
    }

    public boolean isRemember() {
        return remember;
    }

    public void setRemember(boolean remember) {
        this.remember = remember;
    }
}
