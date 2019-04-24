package com.helin.accountingapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.helin.accountingapp.mpChart.StatisticBean;

import java.util.LinkedList;
import java.util.Map;

public class RecordDatabaseHelper extends SQLiteOpenHelper {

    private String TAG = "RecordDatabaseHelper";

    public static final String DB_NAME = "Record";

    private static final String[] categorys = {"一般", "用餐", "酒水", "商店", "购物", "Personal", "游戏", "电影", "社交", "交通",
            "应用", "通讯", "数码", "礼物", "住房", "旅行", "门票", "学习", "医疗", "转账", "一般", "报销", "薪资", "红包", "兼职", "奖金", "投资"};

    private static final String CREATE_RECORD_DB = "create table Record ("
            + "id integer primary key autoincrement, "
            + "uuid text, "
            + "type integer, "
            + "category text, "
            + "remark text, "
            + "amount double, "
            + "time integer, "
            + "date date )";

    public RecordDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_RECORD_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addRecord(RecordBean bean) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("uuid", bean.getUuid());
        values.put("type", bean.getType());
        values.put("category", bean.getCategory());
        values.put("remark", bean.getRemark());
        values.put("amount", bean.getAmount());
        values.put("date", bean.getDate());
        values.put("time", bean.getTimeStamp());
        db.insert(DB_NAME, null, values);
        values.clear();
        Log.d(TAG, bean.getUuid() + "added");
        db.close();
    }

    public void removeRecord(String uuid) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DB_NAME, "uuid = ?", new String[]{uuid});
    }

    public void editRecord(String uuid, RecordBean record) {
        removeRecord(uuid);
        record.setUuid(uuid);
        addRecord(record);
    }

    public LinkedList<RecordBean> readRecords(String dateStr) {
        LinkedList<RecordBean> records = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select DISTINCT * from Record where date = ? order by time asc", new String[]{dateStr});
        records = iteor(cursor, records);
        db.close();
        return records;
    }

    public LinkedList<String> getAvaliableDate() {

        LinkedList<String> dates = new LinkedList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select DISTINCT * from Record order by date asc ", new String[]{});
        if (cursor.moveToFirst()) {
            do {
                String date = cursor.getString(cursor.getColumnIndex("date"));
                if (!dates.contains(date)) {
                    dates.add(date);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return dates;
    }

    public LinkedList<StatisticBean> getStatisticOfExpensiveOrIncome(int type, String date) {
        LinkedList<StatisticBean> totals = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String curDate = DateUtil.getFormattedDate();
        curDate = curDate.substring(0, 7);
        System.out.println(curDate);
        Cursor cursor = db.rawQuery("select category,sum(amount) as sumExpensive from Record where date like '" + curDate + "%' and type=" + type + " group by category", new String[]{});
        if (cursor.moveToFirst()) {
            do {
                String category = cursor.getString(cursor.getColumnIndex("category"));
                Integer sumExpensive = cursor.getInt(cursor.getColumnIndex("sumExpensive"));
                StatisticBean total = new StatisticBean();
                total.setCategory(category);
                total.setAmount(sumExpensive);
                total.setType(type);
                totals.add(total);
            } while (cursor.moveToNext());
            cursor.close();
            db.close();
            return totals;

        }
        return null;
    }

    public LinkedList<RecordBean> getInfoByKeyword(String keyword) {
        LinkedList<RecordBean> infos = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from Record where remark like'%" + keyword + "%'order by date desc,time desc", new String[]{});
        infos = iteor(cursor, infos);
        db.close();
        return infos;
    }

    private LinkedList<RecordBean> iteor(Cursor cursor, LinkedList<RecordBean> records) {
        if (cursor.moveToFirst()) {
            do {
                String uuid = cursor.getString(cursor.getColumnIndex("uuid"));
                int type = cursor.getInt(cursor.getColumnIndex("type"));
                String category = cursor.getString(cursor.getColumnIndex("category"));
                String remark = cursor.getString(cursor.getColumnIndex("remark"));
                double amount = cursor.getDouble(cursor.getColumnIndex("amount"));
                String date = cursor.getString(cursor.getColumnIndex("date"));
                long timeStamp = cursor.getLong(cursor.getColumnIndex("time"));

                RecordBean record = new RecordBean();
                record.setUuid(uuid);
                record.setType(type);
                record.setCategory(category);
                record.setRemark(remark);
                record.setAmount(amount);
                record.setDate(date);
                record.setTimeStamp(timeStamp);

                records.add(record);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return records;
    }

}
