package com.example.locals;

public class Order_model {
    String Username;
    String orderkey;

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    String sum;
    String address;
    String modeOfPayment;
    String uid;
    Boolean status;//true for delivered false for pending
    String delivery_date;
    String delivery_time;


    public String getOrderkey() {
        return orderkey;
    }

    public void setOrderkey(String orderkey) {
        this.orderkey = orderkey;
    }
    String order_date;
    String order_time;

    public String getDelivery_date() {
        return delivery_date;
    }

    public void setDelivery_date(String delivery_date) {
        this.delivery_date = delivery_date;
    }

    public String getDelivery_time() {
        return delivery_time;
    }

    public void setDelivery_time(String delivery_time) {
        this.delivery_time = delivery_time;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }



    public Order_model() {
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getModeOfPayment() {
        return modeOfPayment;
    }

    public void setModeOfPayment(String modeOfPayment) {
        this.modeOfPayment = modeOfPayment;
    }


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
