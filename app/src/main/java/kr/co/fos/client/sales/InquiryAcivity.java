package kr.co.fos.client.sales;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

import kr.co.fos.client.R;

public class InquiryAcivity extends AppCompatActivity {

    LineChart lineChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sales_inquiry);

        lineChart = (LineChart)findViewById(R.id.chart);
        ArrayList<Entry> entry_chart = new ArrayList<>();


        lineChart = (LineChart) findViewById(R.id.chart);//layout의 id
        LineData chartData = new LineData();

        entry_chart.add(new Entry(0, 10000));
        entry_chart.add(new Entry(1, 2));
        entry_chart.add(new Entry(2, 3));
        entry_chart.add(new Entry(3, 4));
    /* 만약 (2, 3) add하고 (2, 5)한다고해서
    기존 (2, 3)이 사라지는게 아니라 x가 2인곳에 y가 3, 5의 점이 찍힘 */

        LineDataSet lineDataSet = new LineDataSet(entry_chart, "꺽은선1");


        lineDataSet.setLineWidth(2); // 선 굵기
        lineDataSet.setCircleRadius(6); // 곡률
        lineDataSet.setCircleColor(Color.parseColor("#FFA1B4DC"));
        lineDataSet.setCircleColorHole(Color.BLUE);
        lineDataSet.setColor(Color.parseColor("#FFA1B4DC"));

        chartData.addDataSet(lineDataSet);



        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.BLACK);
        xAxis.enableGridDashedLine(8, 24, 0);


        YAxis yLAxis = lineChart.getAxisLeft();
        yLAxis.setTextColor(Color.BLACK);
        YAxis yRAxis = lineChart.getAxisRight();
        yRAxis.setDrawLabels(false);
        yRAxis.setDrawAxisLine(false);
        yRAxis.setDrawGridLines(false);


        lineChart.setData(chartData);
        lineChart.invalidate();

    }

    //주문 조회
    public void orderInquiry() {

    }
}
