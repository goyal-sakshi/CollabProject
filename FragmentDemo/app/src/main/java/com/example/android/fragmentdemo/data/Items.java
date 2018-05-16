package com.example.android.fragmentdemo.data;

/**
 * Created by hp on 3/27/2018.
 */

public class Items {

    private String name;
    private String price;
    private String quantity;
    private String imageUrl;

    public Items() {

    }

    public Items(String price, String quantity, String name, String imageUrl) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
    }


    public String getPrice() {
        return price;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}