package kr.co.fos.client.review;

import java.sql.Date;
import java.io.Serializable;

public class Review implements Serializable {
    private String no;
    private String memberNo;
    private String foodtruckNo;
    private String grade;
    private String content;
    private Date registDate;

    public Review() {
    }

    public Review(String no, String memberNo, String foodtruckNo, String grade, String content, Date registDate) {
        this.no = no;
        this.memberNo = memberNo;
        this.foodtruckNo = foodtruckNo;
        this.grade = grade;
        this.content = content;
        this.registDate = registDate;

    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getNo() {
        return this.no;
    }

    public void setMemberNo(String memberNo) {
        this.memberNo = memberNo;
    }

    public String getMemberNo() {
        return this.memberNo;
    }

    public void setFoodtruckNo(String foodtruckNo) {
        this.foodtruckNo = foodtruckNo;
    }

    public String getFoodtruckNo() {
        return this.foodtruckNo;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getGrade() {
        return this.grade;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return this.content;
    }

    public void setRegistDate(Date registDate) {
        this.registDate = registDate;
    }

    public Date getRegistDate() {
        return this.registDate;
    }

}