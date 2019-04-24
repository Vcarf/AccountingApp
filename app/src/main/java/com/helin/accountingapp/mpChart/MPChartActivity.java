package com.helin.accountingapp.mpChart;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.helin.accountingapp.GlobalUtil;
import com.helin.accountingapp.ListViewAdapter;
import com.helin.accountingapp.R;
import com.helin.accountingapp.search.SearchActivity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

public class MPChartActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemClickListener {

    private PieChart mPieChart;
    private TextView mTvIncome,mTvCost;
    private ListView mListView;
    private StatisticListAdapter adapter;
    LinkedList<StatisticBean> statisticOfExpensive;

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
        statisticOfExpensive = GlobalUtil.getInstance().databaseHelper.getStatisticOfExpensiveOrIncome(1,null);
        mPieChart = findViewById(R.id.chart_pie);
        mTvCost=findViewById(R.id.btn_cost);
        mTvIncome=findViewById(R.id.btn_income);
        mListView=findViewById(R.id.mp_listview);

        mTvCost.setOnClickListener(this);
        mTvIncome.setOnClickListener(this);
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

        adapter=new StatisticListAdapter(MPChartActivity.this);
        adapter.setData(statisticOfExpensive);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);

    }

    private PieData getPieData() {
        ArrayList<String> pieText = new ArrayList<>();
        ArrayList<PieEntry> pieDatas = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            pieText.add((i + 1) + "类别");
        }

        if (statisticOfExpensive != null) {
            for (StatisticBean total:statisticOfExpensive) {
                //System.err.println(se.getKey() + "::" + se.getValue());
                pieDatas.add(new PieEntry((float) total.getAmount(), total.getCategory()));
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

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_cost:
                mTvCost.setTextColor(Color.argb(255,212,113,13));
                mTvIncome.setTextColor(Color.BLACK);
                statisticOfExpensive = GlobalUtil.getInstance().databaseHelper.getStatisticOfExpensiveOrIncome(1,null);
                showChart(getPieData());
                break;
            case R.id.btn_income:
                mTvIncome.setTextColor(Color.argb(255,212,113,13));
                mTvCost.setTextColor(Color.BLACK);
                statisticOfExpensive = GlobalUtil.getInstance().databaseHelper.getStatisticOfExpensiveOrIncome(2,null);
                showChart(getPieData());
                break;
        }
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        System.out.println("is running ===========");
        final StatisticBean item=statisticOfExpensive.get(i);
        Intent intent=new Intent(MPChartActivity.this,SearchActivity.class);
        intent.putExtra("category",item.getCategory());
        startActivity(intent);

    }
}
