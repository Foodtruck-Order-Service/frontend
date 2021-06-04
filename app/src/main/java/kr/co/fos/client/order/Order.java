package kr.co.fos.client.order;

import java.sql.Date;
import java.io.Serializable;
import java.util.List;

public class Order implements Serializable {
    private int no;
    private int memberNo;
    private int foodtruckNo;
    private int receptionNo;
    private String orderTime;
    private String totalAmount;
    private String paymentType;
    private String status;
    private int lat;
    private int lng;
    private String merchantUid;
    private List<OrderInfo> orderInfos;

    public Order() {
    }


    @Override
    public String toString() {
        return "Order [no=" + no + ", memberNo=" + memberNo + ", foodtruckNo=" + foodtruckNo + ", receptionNo="
                + receptionNo + ", orderTime=" + orderTime + ", totalAmount=" + totalAmount + ", paymentType="
                + paymentType + ", status=" + status + ", lat=" + lat + ", lng=" + lng + ", merchantUid=" + merchantUid
                + ", orderInfos=" + orderInfos + "]";
    }


    public String getMerchantUid() {
        return merchantUid;
    }


    public void setMerchantUid(String merchantUid) {
        this.merchantUid = merchantUid;
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

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getLat() {
        return lat;
    }

    public void setLat(int lat) {
        this.lat = lat;
    }

    public int getLng() {
        return lng;
    }

    public void setLng(int lng) {
        this.lng = lng;
    }

    public List<OrderInfo> getOrderInfos() {
        return orderInfos;
    }

    public void setOrderInfos(List<OrderInfo> orderInfos) {
        this.orderInfos = orderInfos;
    }
}