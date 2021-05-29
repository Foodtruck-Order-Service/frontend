package kr.co.fos.client.foodtruck;

import java.sql.Date;
import java.io.Serializable;

public class Foodtruck implements Serializable {
    private String no;
    private String memberNo;
    private String brn;
    private String name;
    private String category;
    private String startTime;
    private String endTime;
    private String content;
    private int lat;
    private int lng;
    private String approval;
    private String status;

    private int itemStart;
    private int itemSizePerPage;

    public Foodtruck() {
    }

    public Foodtruck(String no, String memberNo, String brn, String name, String category, String startTime, String endTime, String content, int lat, int lng, String approval, String status, int itemStart, int itemSizePerPage) {
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

        this.itemStart = itemStart;
        this.itemSizePerPage = itemSizePerPage;
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

    public void setLat(int lat) {
        this.lat = lat;
    }

    public int getLat() {
        return this.lat;
    }

    public void setLng(int lng) {
        this.lng = lng;
    }

    public int getLng() {
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

    public void setItemStart(int itemStart) {
        this.itemStart = itemStart;
    }

    public int getItemStart() {
        return this.itemStart;
    }

    public void setItemSizePerPage(int itemSizePerPage) {
        this.itemSizePerPage = itemSizePerPage;
    }

    public int getItemSizePerPage() {
        return this.itemSizePerPage;
    }
}