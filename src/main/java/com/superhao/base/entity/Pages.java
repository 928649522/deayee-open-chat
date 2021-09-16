package com.superhao.base.entity;

import com.baomidou.mybatisplus.plugins.Page;

/**
 * @Auther: zehao
 * @Date: 2019/4/25 18:20
 * @email: 928649522@qq.com
 * @Description:
 */
public class Pages<T> extends Page<T> {
    /**
     * 每页的行记录数
     */
    public final static int ROW_SIZE=10;


    public Pages(int current, int size) {
        super(current, size);
    }

    public static  Pages create(int current){
        return new Pages(current,ROW_SIZE);
    }
    public static  <T>Pages createT(int current){
        return new Pages<T>(current,ROW_SIZE);
    }




}
