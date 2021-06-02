package kr.co.fos.client.bookmark;

import java.sql.Date;
import java.io.Serializable;

public class Bookmark implements Serializable {
    private int no;
    private int memberNo;
    private int foodtruckNo;

    public Bookmark() {
    }

    public Bookmark(int no, int memberNo, int foodtruckNo) {
        this.no = no;
        this.memberNo = memberNo;
        this.foodtruckNo = foodtruckNo;

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

    @Override
    public String toString() {
        return "Bookmark{" +
                "no='" + no + '\'' +
                ", memberNo='" + memberNo + '\'' +
                ", foodtruckNo='" + foodtruckNo + '\'' +
                '}';
    }
}