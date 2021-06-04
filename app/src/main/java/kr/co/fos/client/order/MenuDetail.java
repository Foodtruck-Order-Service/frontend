package kr.co.fos.client.order;

import java.sql.Date;
import java.io.Serializable;

public class MenuDetail implements Serializable {
    private int no;
    private int orderInfoNo;
    private String optionName;
    private String optionValue;
    private String addAmount;


    @Override
    public String toString() {
        return "MenuDetail{" +
                "no=" + no +
                ", orderInfoNo=" + orderInfoNo +
                ", optionName='" + optionName + '\'' +
                ", optionValue='" + optionValue + '\'' +
                ", addAmount='" + addAmount + '\'' +
                '}';
    }

    public MenuDetail() {
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public int getOrderInfoNo() {
        return orderInfoNo;
    }

    public void setOrderInfoNo(int orderInfoNo) {
        this.orderInfoNo = orderInfoNo;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public String getOptionValue() {
        return optionValue;
    }

    public void setOptionValue(String optionValue) {
        this.optionValue = optionValue;
    }

    public String getAddAmount() {
        return addAmount;
    }

    public void setAddAmount(String addAmount) {
        this.addAmount = addAmount;
    }
}