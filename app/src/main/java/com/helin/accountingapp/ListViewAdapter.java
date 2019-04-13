package com.helin.accountingapp;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Space;
import android.widget.TextView;

import java.io.Serializable;
import java.util.LinkedList;

public class ListViewAdapter extends BaseAdapter  {

    private LinkedList<RecordBean> records = new LinkedList<>();

    private LayoutInflater mInflater;
    private Context mContext;
    public static boolean mIsShowDate;

    public ListViewAdapter(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    public ListViewAdapter(Context context,boolean isShowdate){
        this.mContext = context;
        mIsShowDate=isShowdate;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setData(LinkedList<RecordBean> records) {
        this.records = records;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return records.size();
    }

    @Override
    public Object getItem(int position) {
        return records.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.cell_list_view, null);

            RecordBean recordBean = (RecordBean) getItem(position);
            holder = new ViewHolder(convertView, recordBean);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    @Nullable
    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }
}

class ViewHolder {
    TextView remarkTV;
    TextView amountTV;
    TextView timeTV;
    ImageView categoryIcon;
    String divider=" ";

    public ViewHolder(View itemView, RecordBean record) {
        remarkTV = itemView.findViewById(R.id.textView_remark);
        amountTV = itemView.findViewById(R.id.textView_amount);
        timeTV = itemView.findViewById(R.id.textView_time);
        categoryIcon = itemView.findViewById(R.id.imageView_category);

        remarkTV.setText(record.getRemark());

        if (record.getType() == 1) {
            amountTV.setText("- " + record.getAmount());
        } else {
            amountTV.setText("+ " + record.getAmount());
        }
        timeTV.setText(DateUtil.getFormattedTime(record.getTimeStamp()));
        if(ListViewAdapter.mIsShowDate){
            timeTV.setText(record.getDate()+divider+DateUtil.getFormattedTime(record.getTimeStamp()));
        }
        categoryIcon.setImageResource(GlobalUtil.getInstance().getResourceIcon(record.getCategory()));
    }

}