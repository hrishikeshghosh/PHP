package com.example.ecommerce_03;

import java.util.Date;

public class my_orders_items_model {


    private String product_id;
    private String product_image;
    private String product_title;

    private String order_status;
    private String address;
    private String coupon_id;
    private String cutted_price;
    private Date ordered_date;
    private Date packed_date;
    private Date shipped_date;
    private Date delivered_date;
    private Date cancelled_date;
    private String discounted_price;
    private long free_coupons;
    private String full_name;
    private String order_id;
    private String payment_method;
    private String pincode;
    private String product_price;
    private long product_quantity;
    private String user_id;
    private int rating=0;
    private String delivery_price;
    private boolean cancellation_requested;

    public my_orders_items_model(String product_id, String order_status, String address, String coupon_id, String cutted_price, Date ordered_date, Date packed_date, Date shipped_date, Date delivered_date, Date cancelled_date, String discounted_price, long free_coupons, String full_name, String order_id, String payment_method, String pincode, String product_price, long product_quantity, String user_id, String product_image,String product_title,String delivery_price,boolean cancellation_requested) {
        this.product_id = product_id;
        this.order_status = order_status;
        this.address = address;
        this.coupon_id = coupon_id;
        this.cutted_price = cutted_price;
        this.ordered_date = ordered_date;
        this.packed_date = packed_date;
        this.shipped_date = shipped_date;
        this.delivered_date = delivered_date;
        this.cancelled_date = cancelled_date;
        this.discounted_price = discounted_price;
        this.free_coupons = free_coupons;
        this.full_name = full_name;
        this.order_id = order_id;
        this.payment_method = payment_method;
        this.pincode = pincode;
        this.product_price = product_price;
        this.product_quantity = product_quantity;
        this.user_id = user_id;
        this.product_image=product_image;
        this.product_title=product_title;
        this.delivery_price=delivery_price;
        this.cancellation_requested=cancellation_requested;
    }

    public boolean isCancellation_requested() {
        return cancellation_requested;
    }

    public void setCancellation_requested(boolean cancellation_requested) {
        this.cancellation_requested = cancellation_requested;
    }

    public String getDelivery_price() {
        return delivery_price;
    }

    public void setDelivery_price(String delivery_price) {
        this.delivery_price = delivery_price;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getProduct_title() {
        return product_title;
    }

    public void setProduct_title(String product_title) {
        this.product_title = product_title;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCoupon_id() {
        return coupon_id;
    }

    public void setCoupon_id(String coupon_id) {
        this.coupon_id = coupon_id;
    }

    public String getCutted_price() {
        return cutted_price;
    }

    public void setCutted_price(String cutted_price) {
        this.cutted_price = cutted_price;
    }

    public Date getOrdered_date() {
        return ordered_date;
    }

    public void setOrdered_date(Date ordered_date) {
        this.ordered_date = ordered_date;
    }

    public Date getPacked_date() {
        return packed_date;
    }

    public void setPacked_date(Date packed_date) {
        this.packed_date = packed_date;
    }

    public Date getShipped_date() {
        return shipped_date;
    }

    public void setShipped_date(Date shipped_date) {
        this.shipped_date = shipped_date;
    }

    public Date getDelivered_date() {
        return delivered_date;
    }

    public void setDelivered_date(Date delivered_date) {
        this.delivered_date = delivered_date;
    }

    public Date getCancelled_date() {
        return cancelled_date;
    }

    public void setCancelled_date(Date cancelled_date) {
        this.cancelled_date = cancelled_date;
    }

    public String getDiscounted_price() {
        return discounted_price;
    }

    public void setDiscounted_price(String discounted_price) {
        this.discounted_price = discounted_price;
    }

    public long getFree_coupons() {
        return free_coupons;
    }

    public void setFree_coupons(long free_coupons) {
        this.free_coupons = free_coupons;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public long getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(long product_quantity) {
        this.product_quantity = product_quantity;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}