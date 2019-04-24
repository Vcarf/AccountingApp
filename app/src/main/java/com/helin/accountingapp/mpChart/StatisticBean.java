package com.helin.accountingapp.mpChart;

import com.helin.accountingapp.RecordBean;

/**
 * Created by helin on 2019/4/16
 */
public class StatisticBean {
    private String category;
    private Integer amount;
    private RecordBean.RecordType type;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public int getType() {
        if (this.type == RecordBean.RecordType.RECORD_TYPE_EXPENSE){
            return 1;
        }else {
            return 2;
        }
    }

    public void setType(int type) {
        if (type==1){
            this.type = RecordBean.RecordType.RECORD_TYPE_EXPENSE;
        }
        else {
            this.type = RecordBean.RecordType.RECORD_TYPE_INCOME;
        }
    }


}
