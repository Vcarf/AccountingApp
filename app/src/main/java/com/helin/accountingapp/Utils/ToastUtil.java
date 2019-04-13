package com.helin.accountingapp.Utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by helin on 2019/4/2
 */
public class ToastUtil {
    public static Toast mToast;
    public static void showMsg(Context context, String msg){
        if(mToast==null){
            mToast=Toast.makeText(context,msg,Toast.LENGTH_LONG);
        }else{
            mToast.setText(msg);
        }
        mToast.show();
    }
}
