package com.helin.accountingapp.mpChart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.helin.accountingapp.GlobalUtil;
import com.helin.accountingapp.R;

import java.util.LinkedList;

/**
 * Created by helin on 2019/4/16
 */
public class StatisticListAdapter extends BaseAdapter {
    private LinkedList<StatisticBean> totals=new LinkedList<>();
    private Context context;
    private LayoutInflater layoutInflater;

    public StatisticListAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if(totals==null){
            return 0;
        }else {
            return totals.size();
        }

    }

    @Override
    public Object getItem(int i) {
        return totals.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.layout_statistic_item, null);
            StatisticBean total = (StatisticBean) getItem(i);
            holder = new ViewHolder(view, total);
        } else {
            StatisticBean total = (StatisticBean) getItem(i);
            holder = new ViewHolder(view, total);
        }
        view.setTag(holder);
        return view;
    }

    public void setData(LinkedList<StatisticBean> totals) {
        this.totals = totals;
        notifyDataSetChanged();
    }
}

class ViewHolder {
    TextView mTvCategory;
    TextView mTvAmount;
    ImageView categoryIcon;

    public ViewHolder(View itemView, StatisticBean total) {
        mTvCategory = itemView.findViewById(R.id.textView_category);
        mTvAmount = itemView.findViewById(R.id.textView_amount);
        categoryIcon = itemView.findViewById(R.id.imageView_category);

        mTvCategory.setText(total.getCategory());
        if (total.getType() == 1) {
            mTvAmount.setText("+" + total.getAmount());
        } else {
            mTvAmount.setText("-" + total.getAmount());
        }

        categoryIcon.setImageResource(GlobalUtil.getInstance().getResourceIcon(total.getCategory()));
    }

}
