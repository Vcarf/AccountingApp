package com.helin.accountingapp.set;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.helin.accountingapp.DateUtil;
import com.helin.accountingapp.MainActivity;
import com.helin.accountingapp.R;
import com.helin.accountingapp.securityauth.ModifyGestureActivity;

import java.util.Calendar;

import static com.helin.accountingapp.datepicker.DateFormatUtils.long2Str;
import static com.helin.accountingapp.datepicker.DateFormatUtils.str2Long;

public class SetMainActivity extends AppCompatActivity implements View.OnClickListener {
    private RelativeLayout mRlChange, mRlAlert;
    private Intent intent;
    private ImageView mIvAlert;
    private Calendar calendar = Calendar.getInstance();
    public static Integer mHour, mMinute;
    private TextView mTvAlertTime;
    private static Context mContext;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private String notification;
    private String date;

    public static Context getContext() {
        return mContext;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_main);
        mRlChange = findViewById(R.id.rl_change);
        mRlChange.setOnClickListener(this);
        mIvAlert = findViewById(R.id.iv_switch);
        mRlAlert = findViewById(R.id.rl_alert);
        mTvAlertTime = findViewById(R.id.tv_alert_time);
        mPreferences = getSharedPreferences("data", MODE_PRIVATE);
        mEditor = mPreferences.edit();
        if((notification=mPreferences.getString("notification", "false")).equals("true")){
            mIvAlert.setImageResource(R.drawable.ic_btn_open);
            mTvAlertTime.setText(new StringBuilder().append(mPreferences.getString("hour","21")).append(":").append(mPreferences.getString("minutes","00")));

        }
        mContext = this;

        mRlAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notification = mPreferences.getString("notification", "false");
                Log.e("TAG", notification);
                if (notification.equals("false") ) {
                    setDateAlert();
                    mIvAlert.setImageResource(R.drawable.ic_btn_open);
                    mEditor.putString("notification", "true");
                    mEditor.apply();
                } else {
                    mIvAlert.setImageResource(R.drawable.ic_btn_close);
                    PushService.cleanAllNotification();
                    mEditor.putString("notification", "false");
                    mEditor.apply();
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_change:
                intent = new Intent(SetMainActivity.this, ModifyGestureActivity.class);
                break;
        }
        startActivity(intent);
    }

    public long getDelayTime(String setTimeString){
        Calendar calendar=Calendar.getInstance();
    //        System.out.println(setTimeString+"    ========================");
        long setTimeLong=str2Long(setTimeString,true);
//        System.out.println(setTimeLong+"      ++++++++++++++++++++++++");
        if(setTimeLong<calendar.getTimeInMillis()){
            calendar.add(Calendar.DATE,-1);
        }
        return setTimeLong-calendar.getTimeInMillis();
    }

    public void setDateAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(SetMainActivity.this);
        View v = View.inflate(SetMainActivity.this, R.layout.layout_time_dialog, null);
        final TimePicker timePicker = v.findViewById(R.id.time_picker);
        timePicker.setIs24HourView(true);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                mHour=i;
                mMinute=i1;
                date=long2Str(System.currentTimeMillis(),false);
                date+=" "+mHour+":"+mMinute;
                long dealyTime=getDelayTime(date);
//                System.out.println(dealyTime+"[[[[[]]]]]]]][[[[[[[]]]]]]]]");
                PushService.addNotification(dealyTime, "text", "记帐提醒", "该记帐了");
                mEditor.putString("hour",mHour.toString());
                mEditor.putString("minutes",mMinute.toString());
                mEditor.apply();

            }

        });
        calendar.setTimeInMillis(System.currentTimeMillis());
        timePicker.setHour(calendar.get(Calendar.HOUR_OF_DAY));
        timePicker.setMinute(calendar.get(Calendar.MINUTE));
        builder.setView(v).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mTvAlertTime.setText(new StringBuilder().append(mHour).append(":").append(mMinute));
                dialogInterface.cancel();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).create().show();
    }
}

