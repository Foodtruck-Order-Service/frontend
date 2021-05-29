package kr.co.fos.client.review;

import java.sql.Date;
import java.io.Serializable;

public class Review implements Serializable {
    private int no;
    private int memberNo;
    private int foodtruckNo;
    private String grade;
    private String content;
    private Date registDate;

    public Review() {
    }

    public Review(int no, int memberNo, int foodtruckNo, String grade, String content, Date registDate) {
        this.no = no;
        this.memberNo = memberNo;
        this.foodtruckNo = foodtruckNo;
        this.grade = grade;
        this.content = content;
        this.registDate = registDate;

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

    public void setRegistDate(Date registDate) {
        this.registDate = registDate;
    }

    public Date getRegistDate() {
        return this.registDate;
    }

    @Override
    public String toString() {
        return "Review{" +
                "no=" + no +
                ", memberNo=" + memberNo +
                ", foodtruckNo=" + foodtruckNo +
                ", grade='" + grade + '\'' +
                ", content='" + content + '\'' +
                ", registDate=" + registDate +
                '}';
    }
}