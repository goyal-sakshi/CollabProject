package com.example.android.fragmentdemo.data;

/**
 * Created by hp on 4/5/2018.
 */

public class orderHistory {

    String date;
    String totalPaid;

    public orderHistory(){

    }

    public orderHistory(String date, String totalPaid){
        this.date = date;
        this.totalPaid = totalPaid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTotalPaid() {
        return totalPaid;
    }

    public void setTotalPaid(String totalPaid) {
        this.totalPaid = totalPaid;
    }
}
