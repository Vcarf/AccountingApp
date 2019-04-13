package com.helin.accountingapp.search;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.helin.accountingapp.DateUtil;
import com.helin.accountingapp.GlobalUtil;
import com.helin.accountingapp.ListViewAdapter;
import com.helin.accountingapp.R;
import com.helin.accountingapp.RecordBean;
import com.helin.accountingapp.Utils.ToastUtil;
import com.helin.accountingapp.datepicker.CustomDatePicker;
import com.helin.accountingapp.datepicker.DateFormatUtils;

import java.util.LinkedList;

public class DateSelectActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout mBgCalender;
    private TextView mTvDate, mTvIncome, mTvCost;
    private CustomDatePicker mDatePicker;
    private ListView mLvCurrentDay;
    private ListViewAdapter mListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_select);
        mBgCalender = findViewById(R.id.bg_calender);
        mTvDate = findViewById(R.id.tv_year_month);
        mLvCurrentDay = findViewById(R.id.lv_current_day);
        mTvCost = findViewById(R.id.tv_cost_number);
        mTvIncome = findViewById(R.id.tv_income_number);

        mListAdapter = new ListViewAdapter(DateSelectActivity.this);
        mBgCalender.setOnClickListener(this);
        initView();
        initDatePicker();
        mTvDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                LinkedList<RecordBean> records = GlobalUtil.getInstance().databaseHelper.readRecords(editable.toString());
                setContext(records);
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bg_calender:
                mDatePicker.show(mTvDate.getText().toString());
                break;
        }
    }

    private void initDatePicker() {
        long beginTimestamp = DateFormatUtils.str2Long("2015-05-01", false);
        long endTimestamp = System.currentTimeMillis();

        mTvDate.setText(DateFormatUtils.long2Str(endTimestamp, false));

        // 通过时间戳初始化日期，毫秒级别
        mDatePicker = new CustomDatePicker(this, new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                mTvDate.setText(DateFormatUtils.long2Str(timestamp, false));
            }
        }, beginTimestamp, endTimestamp);
        // 不允许点击屏幕或物理返回键关闭
        mDatePicker.setCancelable(true);
        // 不显示时和分
        mDatePicker.setCanShowPreciseTime(false);
        // 不允许循环滚动
        mDatePicker.setScrollLoop(true);
        // 不允许滚动动画
        mDatePicker.setCanShowAnim(true);
    }

    public void initView(){

        LinkedList<RecordBean> records = GlobalUtil.getInstance().databaseHelper.readRecords(DateUtil.getFormattedDate());
        setContext(records);

    }

    public void setContext(LinkedList<RecordBean> records){
        Double cost = 0.00, income = 0.00;
        if (records.isEmpty()) {
            ToastUtil.showMsg(DateSelectActivity.this, "没有记录");
            mLvCurrentDay.getBackground().setTint(Color.argb(255, 4, 168,244));
            mLvCurrentDay.setAdapter(null);
        }else{
            mListAdapter.setData(records);
            mLvCurrentDay.setAdapter(mListAdapter);
            mLvCurrentDay.getBackground().setTint(Color.argb(0,255,255,255));
            for (RecordBean record : records) {
                if (record.getType() == 1) {
                    cost += record.getAmount();
                } else {
                    income += record.getAmount();
                }
            }
        }

        mTvCost.setText(cost.toString());
        mTvIncome.setText(income.toString());
    }


}
