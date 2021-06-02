package kr.co.fos.client.menu;

import java.sql.Date;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class Option implements Serializable {
    private int no;
    private int menuNo;
    private String optionName;

    private List<OptionValue> optionValues;

    public Option() {
    }

    public Option(int no, int menuNo, String optionName, List<OptionValue> optionValues) {
        this.no = no;
        this.menuNo = menuNo;
        this.optionName = optionName;
        this.optionValues = optionValues;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public int getNo() {
        return this.no;
    }

    public void setMenuNo(int menuNo) {
        this.menuNo = menuNo;
    }

    public int getMenuNo() {
        return this.menuNo;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public String getOptionName() {
        return this.optionName;
    }

    public List<OptionValue> getOptionValues() {
        return optionValues;
    }

    public void setOptionValues(List<OptionValue> optionValues) {
        this.optionValues = optionValues;
    }

    @Override
    public String toString() {
        return "Option{" +
                "no=" + no +
                ", menuNo=" + menuNo +
                ", optionName='" + optionName + '\'' +
                ", optionValues=" + Arrays.asList(optionValues).toString() +
                '}';
    }
}