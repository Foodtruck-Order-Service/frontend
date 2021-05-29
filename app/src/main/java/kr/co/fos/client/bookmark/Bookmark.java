package kr.co.fos.client.bookmark;

import java.sql.Date;
import java.io.Serializable;

public class Bookmark implements Serializable {
    private String no;
    private String memberNo;
    private String foodtruckNo;

    public Bookmark() {
    }

    public Bookmark(String no, String memberNo, String foodtruckNo) {
        this.no = no;
        this.memberNo = memberNo;
        this.foodtruckNo = foodtruckNo;

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

}