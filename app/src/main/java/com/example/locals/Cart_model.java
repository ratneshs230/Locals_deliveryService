package com.example.locals;

public class Cart_model {
    String cart_Product_name;
    String cart_Product_qty;
    String cart_Product_price;
    String total_price;
    String cart_key;
    String cart_Product_date;
    String cart_Product_ID;
    String cart_Image;
    String cart_measure;

    public String getCart_measure() {
        return cart_measure;
    }

    public void setCart_measure(String cart_measure) {
        this.cart_measure = cart_measure;
    }

    String userId;

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }



    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCart_Image() {
        return cart_Image;
    }

    public void setCart_Image(String cart_Image) {
        this.cart_Image = cart_Image;
    }

    public String getCart_Product_name() {
        return cart_Product_name;
    }

    public void setCart_Product_name(String cart_Product_name) {
        this.cart_Product_name = cart_Product_name;
    }



    public String getCart_key() {
        return cart_key;
    }

    public String getCart_Product_date() {
        return cart_Product_date;
    }

    public String getCart_Product_ID() {
        return cart_Product_ID;
    }

    public void setCart_key(String cart_key) {
        this.cart_key = cart_key;
    }

    public void setCart_Product_date(String cart_Product_date) {
        this.cart_Product_date = cart_Product_date;
    }

    public void setCart_Product_ID(String cart_Product_ID) {
        this.cart_Product_ID = cart_Product_ID;
    }

    public Cart_model() {
    }

    public String getCart_Product_qty() {
        return cart_Product_qty;
    }

    public void setCart_Product_qty(String cart_Product_qty) {
        this.cart_Product_qty = cart_Product_qty;
    }

    public String getCart_Product_price() {
        return cart_Product_price;
    }

    public void setCart_Product_price(String cart_Product_price) {
        this.cart_Product_price = cart_Product_price;
    }
}
