package kr.co.fos.client.order;

import java.io.Serializable;

public class BusinessListViewItem implements Serializable {
    private int no;
    private int memberNo;
    private int foodtruckNo;
    private String name;
    private String paymentType;
    private int receptionNo;
    private String orderTime;
    private String totalAmount;
    private String status;

    public BusinessListViewItem() {

    }

    @Override
    public String toString() {
        return "BusinessListViewItem [no=" + no + ", memberNo=" + memberNo + ", foodtruckNo=" + foodtruckNo + ", name="
                + name + ", paymentType=" + paymentType + ", receptionNo=" + receptionNo + ", orderTime=" + orderTime
                + ", totalAmount=" + totalAmount + ", status=" + status + "]";
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public int getReceptionNo() {
        return receptionNo;
    }

    public void setReceptionNo(int receptionNo) {
        this.receptionNo = receptionNo;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}