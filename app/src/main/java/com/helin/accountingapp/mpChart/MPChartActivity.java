package com.helin.accountingapp.mpChart;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.helin.accountingapp.GlobalUtil;
import com.helin.accountingapp.R;

import java.util.ArrayList;
import java.util.Map;

public class MPChartActivity extends AppCompatActivity {

    private PieChart mPieChart;
    private BarChart mBarChart;
    Map<String, Integer> statisticOfExpensive;

    public static final int[] PIE_COLORS = {
            Color.rgb(181, 194, 202), Color.rgb(129, 216, 200), Color.rgb(241, 214, 145),
            Color.rgb(108, 176, 223), Color.rgb(195, 221, 155), Color.rgb(251, 215, 191),
            Color.rgb(237, 189, 189), Color.rgb(172, 217, 243),Color.rgb(0, 255 ,255),
            Color.rgb(127 ,255 ,212), Color.rgb(32 ,178 ,170),Color.rgb(0 ,255, 127),
            Color.rgb(255 ,255, 0), Color.rgb(255, 105 ,180),Color.rgb(0, 255 ,0),
            Color.rgb(148 ,0, 211), Color.rgb(105 ,89 ,205),Color.rgb(39, 64 ,139),
            Color.rgb(52 ,245 ,255), Color.rgb(0 ,245 ,255),Color.rgb(232, 232 ,232),

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpchart);
        statisticOfExpensive = GlobalUtil.getInstance().databaseHelper.getStatisticOfExpensive();
        mPieChart = findViewById(R.id.chart_pie);
        mBarChart = findViewById(R.id.chart_bar);
        showChart(getPieData());
    }

    private void showChart(PieData pieData) {
        mPieChart.setHoleRadius(60f);//内环半径
        mPieChart.setTransparentCircleRadius(64f);//半透明圈半径
        Description description = new Description();
        description.setText("当月支出状况");
        mPieChart.setDescription(description);
        mPieChart.setDrawCenterText(true);//设置中间可添加文字
        mPieChart.setCenterText("中间文字");
        mPieChart.setDrawHoleEnabled(true);
        mPieChart.setRotationAngle(90);
        mPieChart.setTouchEnabled(true);
        mPieChart.setData(pieData);
        //取消高亮显示
        mPieChart.highlightValue(null);
        mPieChart.invalidate();
        //设置比例图
        Legend mLegend = mPieChart.getLegend();
        mLegend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        mLegend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        mLegend.setForm(Legend.LegendForm.CIRCLE);
        mLegend.setXEntrySpace(5f);
        mLegend.setYEntrySpace(5f);
        //设置动画
        mPieChart.animateXY(1000, 1000);

    }

    private PieData getPieData() {
        ArrayList<String> pieText = new ArrayList<>();
        ArrayList<PieEntry> pieDatas = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            pieText.add((i + 1) + "类别");
        }

        if (statisticOfExpensive != null) {
            for ( Map.Entry<String, Integer> se : statisticOfExpensive.entrySet()) {
                System.err.println(se.getKey() + "::" + se.getValue());
                pieDatas.add(new PieEntry((float) se.getValue(), se.getKey()));
            }
        }

        //y轴集合
        PieDataSet pieDataSet = new PieDataSet(pieDatas, "");
        pieDataSet.setSliceSpace(0f);
        //饼图颜色
        pieDataSet.setColors(PIE_COLORS);

        // 设置选中态多出的长度
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float px = 5 * (metrics.densityDpi / 160f);
        pieDataSet.setSelectionShift(px);

        // 创建饼图数据
        PieData pieData = new PieData(pieDataSet);

        return pieData;
    }

}
