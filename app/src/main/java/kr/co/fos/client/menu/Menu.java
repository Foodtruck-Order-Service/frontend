package kr.co.fos.client.menu;

import java.sql.Date;
import java.io.Serializable;
import java.util.List;

public class Menu implements Serializable {
    private String no;
    private String foodtruckNo;
    private String name;
    private String amount;
    private String cookingTime;
    private String content;

    private List<Option> options;
    public Menu() {
    }

    public Menu(String no, String foodtruckNo, String name, String amount, String cookingTime, String content, List<Option> options) {
        this.no = no;
        this.foodtruckNo = foodtruckNo;
        this.name = name;
        this.amount = amount;
        this.cookingTime = cookingTime;
        this.content = content;
        this.options = options;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getNo() {
        return this.no;
    }

    public void setFoodtruckNo(String foodtruckNo) {
        this.foodtruckNo = foodtruckNo;
    }

    public String getFoodtruckNo() {
        return this.foodtruckNo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAmount() {
        return this.amount;
    }

    public void setCookingTime(String cookingTime) {
        this.cookingTime = cookingTime;
    }

    public String getCookingTime() {
        return this.cookingTime;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return this.content;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }
}