package kr.co.fos.client.menu;

import java.sql.Date;
import java.io.Serializable;

public class OptionValue implements Serializable {
    private String no;
    private String optionNo;
    private String optionValue;
    private String addAmount;

    private int itemStart;
    private int itemSizePerPage;

    public OptionValue() {
    }

    public OptionValue(String no, String optionNo, String optionValue, String addAmount, int itemStart, int itemSizePerPage) {
        this.no = no;
        this.optionNo = optionNo;
        this.optionValue = optionValue;
        this.addAmount = addAmount;

        this.itemStart = itemStart;
        this.itemSizePerPage = itemSizePerPage;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getNo() {
        return this.no;
    }

    public void setOptionNo(String optionNo) {
        this.optionNo = optionNo;
    }

    public String getOptionNo() {
        return this.optionNo;
    }

    public void setOptionValue(String optionValue) {
        this.optionValue = optionValue;
    }

    public String getOptionValue() {
        return this.optionValue;
    }

    public void setAddAmount(String addAmount) {
        this.addAmount = addAmount;
    }

    public String getAddAmount() {
        return this.addAmount;
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