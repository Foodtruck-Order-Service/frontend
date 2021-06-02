package kr.co.fos.client.basket;

import android.graphics.drawable.Drawable;

import java.sql.Date;
import java.util.List;

import kr.co.fos.client.menu.Option;
import kr.co.fos.client.order.OrderInfo;

public class ListViewItem {
    private int no;
    private int memberNo;
    private int foodtruckNo;
    private int receptionNo;
    private String name;
    private List<Option> options;
    private String amount;
    private int count;

    public ListViewItem() {
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public int getMemberNo() {
        return memberNo;
    }

    public void setMemberNo(int memberNo) {
        this.memberNo = memberNo;
    }

    public int getFoodtruckNo() {
        return foodtruckNo;
    }

    public void setFoodtruckNo(int foodtruckNo) {
        this.foodtruckNo = foodtruckNo;
    }

    public int getReceptionNo() {
        return receptionNo;
    }

    public void setReceptionNo(int receptionNo) {
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

}