package com.simple.greendaodemo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class CardBean {

    private String schoolName;
    private int number;
    private String grade = "三";
    @Generated(hash = 608980517)
    public CardBean(String schoolName, int number, String grade) {
        this.schoolName = schoolName;
        this.number = number;
        this.grade = grade;
    }
    @Generated(hash = 516506924)
    public CardBean() {
    }
    public String getSchoolName() {
        return this.schoolName;
    }
    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }
    public int getNumber() {
        return this.number;
    }
    public void setNumber(int number) {
        this.number = number;
    }
    public String getGrade() {
        return this.grade;
    }
    public void setGrade(String grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "CardBean{" +
                "schoolName='" + schoolName + '\'' +
                ", number=" + number +
                ", grade='" + grade + '\'' +
                '}';
    }
}
