package kr.co.fos.client.menu;

import java.sql.Date;
import java.io.Serializable;

public class OptionValue implements Serializable {
    private int no;
    private int optionNo;
    private String optionValue;
    private String addAmount;
    private boolean isChecked;

    public OptionValue() {

    }

    public OptionValue(int no, int optionNo, String optionValue, String addAmount, boolean isChecked) {
        this.no = no;
        this.optionNo = optionNo;
        this.optionValue = optionValue;
        this.addAmount = addAmount;
        this.isChecked = isChecked;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public int getNo() {
        return this.no;
    }

    public void setOptionNo(int optionNo) {
        this.optionNo = optionNo;
    }

    public int getOptionNo() {
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

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public String toString() {
        return "OptionValue{" +
                "no=" + no +
                ", optionNo=" + optionNo +
                ", optionValue='" + optionValue + '\'' +
                ", addAmount='" + addAmount + '\'' +
                ", isChecked=" + isChecked +
                '}';
    }
}