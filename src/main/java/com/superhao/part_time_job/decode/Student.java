package com.superhao.part_time_job.decode;

/**
 * @Auther: zehao
 * @Date: 2019/10/2 22:22
 * @email: 928649522@qq.com
 */
public class Student {
    private String name;
    private int age;

    public Student( ) {

    }

    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
