package kr.co.fos.client.foodtruck;

import java.sql.Date;
import java.io.Serializable;

public class Foodtruck implements Serializable {
    private int no;
    private int memberNo;
    private String brn;
    private String name;
    private String category;
    private String startTime;
    private String endTime;
    private String content;
    private double lat;
    private double lng;
    private String approval;
    private String status;

    public Foodtruck() {
    }

    public Foodtruck(int no, int memberNo, String brn, String name, String category, String startTime, String endTime, String content, double lat, double lng, String approval, String status, int itemStart, int itemSizePerPage) {
        this.no = no;
        this.memberNo = memberNo;
        this.brn = brn;
        this.name = name;
        this.category = category;
        this.startTime = startTime;
        this.endTime = endTime;
        this.content = content;
        this.lat = lat;
        this.lng = lng;
        this.approval = approval;
        this.status = status;
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

    public void setBrn(String brn) {
        this.brn = brn;
    }

    public String getBrn() {
        return this.brn;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return this.category;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return this.content;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLat() {
        return this.lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLng() {
        return this.lng;
    }

    public void setApproval(String approval) {
        this.approval = approval;
    }

    public String getApproval() {
        return this.approval;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }
}