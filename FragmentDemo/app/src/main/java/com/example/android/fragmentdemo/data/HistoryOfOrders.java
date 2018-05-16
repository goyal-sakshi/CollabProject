package com.example.android.fragmentdemo.data;

/**
 * Created by hp on 4/21/2018.
 */

public class HistoryOfOrders {

    private String date;
    private String totalPaid;

    public HistoryOfOrders() {

    }

    public HistoryOfOrders(String date, String totalPaid) {
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
