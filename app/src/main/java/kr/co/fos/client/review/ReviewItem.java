package kr.co.fos.client.review;

import java.sql.Date;

public class ReviewItem {
    private int no;
    private String grade;
    private String content;
    private Date registDate;

    public ReviewItem(){

    };

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getRegistDate() {
        return registDate;
    }

    public void setRegistDate(Date registDate) {
        this.registDate = registDate;
    }

    @Override
    public String toString() {
        return "ReviewItem{" +
                "no=" + no +
                ", grade='" + grade + '\'' +
                ", content='" + content + '\'' +
                ", registDate=" + registDate +
                '}';
    }
}
