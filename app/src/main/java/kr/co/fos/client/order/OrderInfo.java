package kr.co.fos.client.order;

import java.sql.Date;
import java.io.Serializable;
import java.util.List;

public class OrderInfo implements Serializable {
    private int no;
    private int orderNo;
    private String menuName;
    private int count;
    private String amount;
    private List<MenuDetail> menuDetails;


    @Override
    public String toString() {
        return "OrderInfo{" +
                "no=" + no +
                ", orderNo=" + orderNo +
                ", menuName='" + menuName + '\'' +
                ", count=" + count +
                ", amount='" + amount + '\'' +
                ", menuDetails=" + menuDetails +
                '}';
    }

    public OrderInfo() {
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public int getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public List<MenuDetail> getMenuDetails() {
        return menuDetails;
    }

    public void setMenuDetails(List<MenuDetail> menuDetails) {
        this.menuDetails = menuDetails;
    }
}