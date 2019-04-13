package com.helin.accountingapp.set;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.helin.accountingapp.R;
import com.helin.accountingapp.securityauth.ModifyGestureActivity;

import java.util.Calendar;

public class SetMainActivity extends AppCompatActivity implements View.OnClickListener {
    private RelativeLayout mRlChange,mRlAlert;
    private Intent intent;
    private ImageView mIvAlert;
    private Calendar calendar=Calendar.getInstance();
    private Integer mHour,mMinute;
    private TextView mTvAlertTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_main);
        mRlChange=findViewById(R.id.rl_change);
        mRlChange.setOnClickListener(this);
        mIvAlert=findViewById(R.id.iv_switch);
        mRlAlert=findViewById(R.id.rl_alert);
        mTvAlertTime=findViewById(R.id.tv_alert_time);
        mRlAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(SetMainActivity.this);
                View v=View.inflate(SetMainActivity.this,R.layout.layout_time_dialog,null);
                final TimePicker timePicker=v.findViewById(R.id.time_picker);
                timePicker.setIs24HourView(true);
                calendar.setTimeInMillis(System.currentTimeMillis());
                timePicker.setHour(calendar.get(Calendar.HOUR_OF_DAY));
                timePicker.setMinute(calendar.get(Calendar.MINUTE));
                builder.setView(v).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                       mHour=timePicker.getHour();
                       mMinute=timePicker.getMinute();
                       mTvAlertTime.setText(new StringBuilder().append("提醒时间 ").append(mHour).append(':').append(mMinute));
                       dialogInterface.cancel();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create().show();

                mIvAlert.setImageResource(R.drawable.ic_btn_open);
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rl_change:
               intent=new Intent(SetMainActivity.this,ModifyGestureActivity.class);
               break;
        }
        startActivity(intent);
    }
}
