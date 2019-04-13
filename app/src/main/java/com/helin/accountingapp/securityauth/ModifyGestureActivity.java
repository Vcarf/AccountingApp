package com.helin.accountingapp.securityauth;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.gestruelock.ExpandLockView;
import com.helin.accountingapp.MainActivity;
import com.helin.accountingapp.R;
import com.helin.accountingapp.Utils.ToastUtil;

public class ModifyGestureActivity extends AppCompatActivity implements View.OnClickListener,ExpandLockView.OnLockPanelListener,ExpandLockView.OnUpdateMessageListener,ExpandLockView.OnFinishDrawPasswordListener {

    private TextView mTvFingerAuth,mTvForgetPassword;
    private FingerprintManagerCompat fingerprintManagerCompat;
    private FingerprintManagerCompat.AuthenticationCallback callback;
    private Handler handler,textHandler;
    private ExpandLockView mExpandLockView;
    private Animation mShakeAnimal;

    private final String succeeMsg="再次输入密码,密码已设置,密码正确,密码正确,请输入新密码";


    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger_auth);
        mTvFingerAuth = findViewById(R.id.tv_finger_auth);
        textHandler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                mTvFingerAuth.setText(null);
            }
        };

        //忘记密码
        mTvForgetPassword=findViewById(R.id.tv_forget_password);
        mTvForgetPassword.setOnClickListener(this);
        //手势解锁
        mExpandLockView=findViewById(R.id.lockviewExpand);
        //震动
        Vibrator mVibrator = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);
        mShakeAnimal=AnimationUtils.loadAnimation(ModifyGestureActivity.this,R.anim.shake);
        mExpandLockView.setActionMode(2);//设置手势密码
        mExpandLockView.setShowError(true);//显示失败视图
        mExpandLockView.setOnLockPanelListener(ModifyGestureActivity.this);
        mExpandLockView.setOnUpdateMessageListener(ModifyGestureActivity.this);
        mExpandLockView.setOnFinishDrawPasswordListener(ModifyGestureActivity.this);

    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.tv_forget_password:
                AlertDialog.Builder builder=new AlertDialog.Builder(ModifyGestureActivity.this);
                builder.setIcon(R.drawable.finger_auth).setTitle("使用指纹验证").setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
        }
    }

    @Override
    public void onLockPanel() {

    }

    @Override
    public void onSetPassword() {
        ToastUtil.showMsg(ModifyGestureActivity.this,"密码设置成功");
    }

    @Override
    public void onOpenLock() {
        Intent intent=new Intent(ModifyGestureActivity.this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onUpdateMessage(String message) {
        if(succeeMsg.contains(message)){
            mTvFingerAuth.setTextColor(0xff434242);
        }else{
            mTvFingerAuth.setAnimation(mShakeAnimal);//动画效果
        }
        mTvFingerAuth.setText(message);
    }

    @Override
    public void vibration(String time) {

    }

    public void fingerAuth(){
        fingerprintManagerCompat = FingerprintManagerCompat.from(ModifyGestureActivity.this);
        callback = new FingerprintManagerCompat.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errMsgId, CharSequence errString) {
                super.onAuthenticationError(errMsgId, errString);
                mTvFingerAuth.setText("验证错误,请30s后重试");
                textHandler.sendMessageDelayed(new Message(),1000*2);
                handler.sendMessageDelayed(new Message(), 1000 * 30);

            }

            @Override
            public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
                super.onAuthenticationHelp(helpMsgId, helpString);
            }

            @Override
            public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);

            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                mTvFingerAuth.setText("验证失败,请重试");
                textHandler.sendMessageDelayed(new Message(),1000*2);
            }
        };
        fingerprintManagerCompat.authenticate(null, 0, null, callback, null);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Log.e("Finger", "重启指纹模块");
                fingerprintManagerCompat.authenticate(null, 0, null, callback, handler);
            }
        };
        textHandler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                mTvFingerAuth.setText(null);
            }
        };

    }
}
