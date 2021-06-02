package kr.co.fos.client.menu;

import java.sql.Date;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Menu implements Serializable {
    private int no;
    private int foodtruckNo;
    private String name;
    private String amount;
    private String cookingTime;
    private String content;

    private List<Option> options;

    public Menu() {
    }

    public Menu(int no, int foodtruckNo, String name, String amount, String cookingTime, String content, List<Option> options) {
        this.no = no;
        this.foodtruckNo = foodtruckNo;
        this.name = name;
        this.amount = amount;
        this.cookingTime = cookingTime;
        this.content = content;
        this.options = options;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public int getNo() {
        return this.no;
    }

    public void setFoodtruckNo(int foodtruckNo) {
        this.foodtruckNo = foodtruckNo;
    }

    public int getFoodtruckNo() {
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

    @Override
    public String toString() {
        return "Menu{" +
                "no=" + no +
                ", foodtruckNo=" + foodtruckNo +
                ", name='" + name + '\'' +
                ", amount='" + amount + '\'' +
                ", cookingTime='" + cookingTime + '\'' +
                ", content='" + content + '\'' +
                ", options=" + Arrays.asList(options).toString() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Menu)) return false;
        Menu menu = (Menu) o;
        return no == menu.no &&
                foodtruckNo == menu.foodtruckNo &&
                Objects.equals(name, menu.name) &&
                Objects.equals(amount, menu.amount) &&
                Objects.equals(cookingTime, menu.cookingTime) &&
                Objects.equals(content, menu.content) &&
                Objects.equals(options, menu.options);
    }

    @Override
    public int hashCode() {
        return Objects.hash(no, foodtruckNo, name, amount, cookingTime, content, options);
    }
}