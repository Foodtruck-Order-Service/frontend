package kr.co.fos.client.order;

import java.sql.Date;
import java.io.Serializable;
import java.util.List;

public class OrderInfo implements Serializable {
    private String no;
    private String orderNo;
    private String menuName;
    private String count;
    private String amount;
    private List<MenuDetail> menuDetails;

    public OrderInfo() {
    }

    public OrderInfo(String no, String orderNo, String menuName, String count, String amount, List<MenuDetail> menuDetails) {
        this.no = no;
        this.orderNo = orderNo;
        this.menuName = menuName;
        this.count = count;
        this.amount = amount;
        this.menuDetails = menuDetails;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getNo() {
        return this.no;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderNo() {
        return this.orderNo;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuName() {
        return this.menuName;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getCount() {
        return this.count;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAmount() {
        return this.amount;
    }

    public List<MenuDetail> getMenuDetails() {
        return menuDetails;
    }

    public void setMenuDetails(List<MenuDetail> menuDetails) {
        this.menuDetails = menuDetails;
    }
}