package kr.co.fos.client.menu;

import java.sql.Date;
import java.io.Serializable;
import java.util.List;

public class Option implements Serializable {
    private String no;
    private String menuNo;
    private String optionName;

    private int itemStart;
    private int itemSizePerPage;
    private List<OptionValue> optionValues;

    public Option() {
    }

    public Option(String no, String menuNo, String optionName, List<OptionValue> optionValues) {
        this.no = no;
        this.menuNo = menuNo;
        this.optionName = optionName;
        this.optionValues = optionValues;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getNo() {
        return this.no;
    }

    public void setMenuNo(String menuNo) {
        this.menuNo = menuNo;
    }

    public String getMenuNo() {
        return this.menuNo;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public String getOptionName() {
        return this.optionName;
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

    public List<OptionValue> getOptionValues() {
        return optionValues;
    }

    public void setOptionValues(List<OptionValue> optionValues) {
        this.optionValues = optionValues;
    }
}