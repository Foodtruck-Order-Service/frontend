package kr.co.fos.client.basket;

import android.graphics.drawable.Drawable;

import java.sql.Date;
import java.util.List;

import kr.co.fos.client.menu.Option;
import kr.co.fos.client.order.OrderInfo;

public class ListViewItem {
    private String no;
    private String memberNo;
    private String foodtruckNo;
    private String receptionNo;
    private String name;
    private List<Option> options;
    private int amount;
    private int count;

    public ListViewItem() {
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getMemberNo() {
        return memberNo;
    }

    public void setMemberNo(String memberNo) {
        this.memberNo = memberNo;
    }

    public String getFoodtruckNo() {
        return foodtruckNo;
    }

    public void setFoodtruckNo(String foodtruckNo) {
        this.foodtruckNo = foodtruckNo;
    }

    public String getReceptionNo() {
        return receptionNo;
    }

    public void setReceptionNo(String receptionNo) {
        this.receptionNo = receptionNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}