package com.helin.accountingapp.search;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.helin.accountingapp.GlobalUtil;
import com.helin.accountingapp.ListViewAdapter;
import com.helin.accountingapp.R;
import com.helin.accountingapp.RecordBean;
import com.helin.accountingapp.Utils.ToastUtil;

import java.util.LinkedList;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEtText;
    private TextView mTvCancel;
    private ImageView mIvCleanEdittext;
    private ListView mLvDetails;
    private ListViewAdapter mListViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mEtText=findViewById(R.id.et_text);
        mTvCancel=findViewById(R.id.cancel);
        mLvDetails=findViewById(R.id.lv_detail);
        mIvCleanEdittext=findViewById(R.id.clean_edittext);
        mListViewAdapter=new ListViewAdapter(SearchActivity.this,true);
        mEtText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i==EditorInfo.IME_ACTION_SEARCH || (keyEvent!=null && keyEvent.getKeyCode()==KeyEvent.KEYCODE_SEARCH)){
                    LinkedList<RecordBean> infos = GlobalUtil.getInstance().databaseHelper.getInfoByKeyword(mEtText.getText().toString());
                    for(RecordBean bean:infos){
                        System.out.println(bean.getRemark()+bean.getAmount()+bean.getDate());
                    }
                    mListViewAdapter.setData(infos);
                    mLvDetails.setAdapter(mListViewAdapter);
                }
                return false;
            }
        });

        mIvCleanEdittext.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.cancel:
                break;
            case R.id.clean_edittext:
                mEtText.setText(null);
                break;
        }
    }
}
