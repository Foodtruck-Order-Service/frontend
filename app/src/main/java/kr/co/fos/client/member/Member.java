package kr.co.fos.client.member;

import java.sql.Date;
import java.io.Serializable;

import kr.co.fos.client.foodtruck.Foodtruck;

public class Member implements Serializable {
    private int no;
    private String id;
    private String password;
    private String rrn;
    private String name;
    private String email;
    private String phone;
    private String type;
    private Foodtruck foodtruck;

    public Member() {
    }

    public Member(int no, String id, String password, String rrn, String name, String email, String phone, String type, Foodtruck foodtruck) {
        this.no = no;
        this.id = id;
        this.password = password;
        this.rrn = rrn;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.type = type;
        this.foodtruck = foodtruck;
    }

    @Override
    public String toString() {
        return "Member{" +
                "no='" + no + '\'' +
                ", id='" + id + '\'' +
                ", password='" + password + '\'' +
                ", rrn='" + rrn + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", type='" + type + '\'' +
                ", foodtruck=" + foodtruck +
                '}';
    }

    public void setNo(int no) {
        this.no = no;
    }

    public int getNo() {
        return this.no;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    public String getRrn() {
        return this.rrn;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public Foodtruck getFoodtruck() {
        return foodtruck;
    }

    public void setFoodtruck(Foodtruck foodtruck) {
        this.foodtruck = foodtruck;
    }
}