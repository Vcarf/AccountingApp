package com.helin.accountingapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.LinkedList;

@SuppressLint("ValidFragment")
public class MainFragment extends Fragment implements AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {

    private View rootView;
    private TextView textView;
    private ListView listView;
    private ListViewAdapter listViewAdapter;

    private static LinkedList<RecordBean> records;
    private Activity activityCompat;
    private String date;

    public MainFragment() {

    }

    @SuppressLint("ValidFragment")
    public MainFragment(String date) {
        this.date = date;
        records = GlobalUtil.getInstance().databaseHelper.readRecords(date);
    }

    public static MainFragment newInstance(String date) {
        Bundle agrs = new Bundle();
        agrs.putString("data", date);
        MainFragment fragment = new MainFragment();
        fragment.setArguments(agrs);
        records = GlobalUtil.getInstance().databaseHelper.readRecords(date);
        return fragment;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        initView();
        return rootView;
    }

    public void reload() {

        records = GlobalUtil.getInstance().databaseHelper.readRecords(date);
        if (listViewAdapter == null) {
            listViewAdapter = new ListViewAdapter(getActivity().getApplicationContext());
        }
        listViewAdapter.setData(records);
        listView.setAdapter(listViewAdapter);

        if (listViewAdapter.getCount() > 0) {
            rootView.findViewById(R.id.no_record_layout).setVisibility(View.INVISIBLE);
        }
    }


    private void initView() {
        textView = rootView.findViewById(R.id.day_text);
        listView = rootView.findViewById(R.id.listView);
        textView.setText(date);
        listViewAdapter = new ListViewAdapter(getContext());
        listViewAdapter.setData(records);
        listView.setAdapter(listViewAdapter);

        if (listViewAdapter.getCount() > 0) {
            rootView.findViewById(R.id.no_record_layout).setVisibility(View.INVISIBLE);
        }

        textView.setText(DateUtil.getDateTitle(date));

        listView.setOnItemLongClickListener(this);
    }

    public int getTotalCost() {
        double totalCost = 0;
        for (RecordBean record : records) {
            if (record.getType() == 1) {
                totalCost += record.getAmount();
            }
        }
        return (int) totalCost;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        showDialog(position);
        return false;
    }


    private void showDialog(int index) {
        final String[] options = {"Remove", "Edit"};
        final RecordBean selectedRecord = records.get(index);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.create();
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    String uuid = selectedRecord.getUuid();
                    GlobalUtil.getInstance().databaseHelper.removeRecord(uuid);
                    reload();
                    GlobalUtil.getInstance().mainActivity.updateHeader();
                } else if (which == 1) {
                    Intent intent = new Intent(getActivity(), AddRecordActivity.class);
                    Bundle extra = new Bundle();
                    extra.putSerializable("record", selectedRecord);
                    intent.putExtras(extra);
                    startActivityForResult(intent, 1);
                }
            }
        });
        builder.setNegativeButton("取消", null);
        builder.create().show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activityCompat = (Activity) context;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}
