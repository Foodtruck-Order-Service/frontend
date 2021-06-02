package kr.co.fos.client.sales;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

public class GraphYAxisValueFormatter implements IAxisValueFormatter {
    private String[] mEmojis;

    // 생성자 초기화
    GraphYAxisValueFormatter(String[] values) {
        this.mEmojis = values;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return mEmojis[(int) value];
    }
}
