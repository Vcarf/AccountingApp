package com.helin.accountingapp.set;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.gestruelock.ExpandLockView;
import com.helin.accountingapp.MainActivity;
import com.helin.accountingapp.R;
import com.helin.accountingapp.Utils.ToastUtil;

public class ModifyGestureActivity1 extends AppCompatActivity implements View.OnClickListener, ExpandLockView.OnLockPanelListener, ExpandLockView.OnUpdateMessageListener, ExpandLockView.OnFinishDrawPasswordListener {
    private ExpandLockView mExpandLockView;
    private Animation mShakeAnimal;
    private TextView mTvMessage;
    private final String succeeMsg = "再次输入密码,密码已设置,密码正确,密码正确,请输入新密码";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_gesture);
        //手势解锁
        mExpandLockView = findViewById(R.id.lockviewExpand);
        //震动
        Vibrator mVibrator = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);
        mShakeAnimal = AnimationUtils.loadAnimation(ModifyGestureActivity1.this, R.anim.shake);
        mExpandLockView.setActionMode(0);//设置手势密码
        mExpandLockView.setActionMode(2);//修改手势密码

        mExpandLockView.setShowError(true);//显示失败视图
        mExpandLockView.setOnLockPanelListener(ModifyGestureActivity1.this);
        mExpandLockView.setOnUpdateMessageListener(ModifyGestureActivity1.this);
        mExpandLockView.setOnFinishDrawPasswordListener(ModifyGestureActivity1.this);

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onSetPassword() {
        ToastUtil.showMsg(ModifyGestureActivity1.this, "密码设置成功");
    }

    @Override
    public void onOpenLock() {
        Intent intent=new Intent(ModifyGestureActivity1.this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onLockPanel() {

    }

    @Override
    public void onUpdateMessage(String message) {
        if (succeeMsg.contains(message)) {
            mTvMessage.setTextColor(0xff434242);
        } else {
            mTvMessage.setAnimation(mShakeAnimal);//动画效果
        }
        mTvMessage.setText(message);
    }

    @Override
    public void vibration(String time) {

    }
}
