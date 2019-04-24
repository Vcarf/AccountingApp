package com.helin.accountingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.helin.accountingapp.mpChart.MPChartActivity;
import com.helin.accountingapp.search.DateSelectActivity;
import com.helin.accountingapp.search.SearchActivity;
import com.helin.accountingapp.set.SetMainActivity;
import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private static final String TAG = "MainActivity";

    private ViewPager viewPager;
    private MainViewPagerAdapter pagerAdapter;
    private TickerView amountText;
    private TextView dateText;
    private ImageView mBtnStatistic, mIvSearch;
    private ImageView mIvMy;
    private TextView mTvMy;
    private RelativeLayout mRlDate;


    private int currentPagerPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GlobalUtil.getInstance().setContext(getApplicationContext());
        GlobalUtil.getInstance().mainActivity = this;
        getSupportActionBar().setElevation(0);

        amountText = findViewById(R.id.amount_text);
        amountText.setCharacterLists(TickerUtils.provideNumberList());
        dateText = findViewById(R.id.date_text);

        mBtnStatistic = findViewById(R.id.btn_statistic);
        mIvSearch = findViewById(R.id.iv_search);
        mIvMy = findViewById(R.id.iv_my);
        mTvMy = findViewById(R.id.tv_my);
        mRlDate = findViewById(R.id.amount_layout);
        mBtnStatistic.setOnClickListener(this);
        mIvSearch.setOnClickListener(this);
        mIvMy.setOnClickListener(this);
        mTvMy.setOnClickListener(this);
        mRlDate.setOnClickListener(this);


        viewPager = findViewById(R.id.view_pager);
        pagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager());
        pagerAdapter.notifyDataSetChanged();
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(this);
        viewPager.setCurrentItem(pagerAdapter.getLatsIndex());

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddRecordActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        updateHeader();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        pagerAdapter.reload();
        updateHeader();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Log.d(TAG, "cost: " + pagerAdapter.getTotalCost(position));
        currentPagerPosition = position;
        updateHeader();
    }

    public void updateHeader() {
        String amount = String.valueOf(pagerAdapter.getTotalCost(currentPagerPosition));
        amountText.setText(amount);
        String date = pagerAdapter.getDateStr(currentPagerPosition);
        dateText.setText(DateUtil.getWeekDay(date));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.iv_my:
            case R.id.tv_my:
                intent = new Intent(MainActivity.this, SetMainActivity.class);
                break;
            case R.id.btn_statistic:
                intent = new Intent(MainActivity.this, MPChartActivity.class);
                break;
            case R.id.iv_search:
                intent = new Intent(MainActivity.this, SearchActivity.class);
                break;
            case R.id.amount_layout:
                intent = new Intent(MainActivity.this, DateSelectActivity.class);
                break;
        }
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println(" protected void onStart() {");
    }
}
