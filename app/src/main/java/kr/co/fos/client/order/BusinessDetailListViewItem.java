package kr.co.fos.client.order;

import java.io.Serializable;
import java.util.List;

import kr.co.fos.client.menu.Option;

public class BusinessDetailListViewItem implements Serializable {
    private int no;
    private int count;
    private String name;
    private List<MenuDetail> menuDetail;
    private String amount;


    @Override
    public String toString() {
        return "BusinessDetailListViewItem{" +
                "no=" + no +
                ", count=" + count +
                ", name='" + name + '\'' +
                ", menuDetail=" + menuDetail +
                ", amount='" + amount + '\'' +
                '}';
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MenuDetail> getMenuDetail() {
        return menuDetail;
    }

    public void setMenuDetail(List<MenuDetail> menuDetail) {
        this.menuDetail = menuDetail;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
