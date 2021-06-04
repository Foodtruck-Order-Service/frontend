package kr.co.fos.client.review;

import java.sql.Date;
import java.io.Serializable;
import java.util.Arrays;

import kr.co.fos.client.member.Member;

public class Review implements Serializable {
    private int no;
    private int memberNo;
    private int foodtruckNo;
    private String grade;
    private String content;
    private String registDate;
    private String logical;
    private String physical;

    private String id;

    public Review() {
    }

    public Review(int no, int memberNo, int foodtruckNo, String grade, String content, String registDate, String id, String logical, String physical) {
        this.no = no;
        this.memberNo = memberNo;
        this.foodtruckNo = foodtruckNo;
        this.grade = grade;
        this.content = content;
        this.registDate = registDate;
        this.id = id;
        this.logical = logical;
        this.physical = physical;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public int getNo() {
        return this.no;
    }

    public void setMemberNo(int memberNo) {
        this.memberNo = memberNo;
    }

    public int getMemberNo() {
        return this.memberNo;
    }

    public void setFoodtruckNo(int foodtruckNo) {
        this.foodtruckNo = foodtruckNo;
    }

    public int getFoodtruckNo() {
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

    public void setRegistDate(String registDate) {
        this.registDate = registDate;
    }

    public String getRegistDate() {
        return this.registDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogical() {
        return logical;
    }

    public void setLogical(String logical) {
        this.logical = logical;
    }

    public String getPhysical() {
        return physical;
    }

    public void setPhysical(String physical) {
        this.physical = physical;
    }

    @Override
    public String toString() {
        return "Review{" +
                "no=" + no +
                ", memberNo=" + memberNo +
                ", foodtruckNo=" + foodtruckNo +
                ", grade='" + grade + '\'' +
                ", content='" + content + '\'' +
                ", registDate='" + registDate + '\'' +
                ", logical='" + logical + '\'' +
                ", physical='" + physical + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}