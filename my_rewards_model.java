package com.example.ecommerce_03;

import java.util.Date;

public class my_rewards_model {
    private String type;
    private String lower_limit;
    private String upper_limit;
    private String discount_or_amount;
    private String coupon_body;
    private Date timestamp;
    private boolean already_used;
    private String coupon_id;

    public my_rewards_model(String coupon_id,String type, String lower_limit, String upper_limit, String discount_or_amount, String coupon_body, Date timestamp,boolean already_used) {
        this.coupon_id=coupon_id;
        this.type = type;
        this.lower_limit = lower_limit;
        this.upper_limit = upper_limit;
        this.discount_or_amount = discount_or_amount;
        this.coupon_body = coupon_body;
        this.timestamp = timestamp;
        this.already_used=already_used;
    }

    public String getCoupon_id() {
        return coupon_id;
    }

    public void setCoupon_id(String coupon_id) {
        this.coupon_id = coupon_id;
    }

    public boolean isAlready_used() {
        return already_used;
    }

    public void setAlready_used(boolean already_used) {
        this.already_used = already_used;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLower_limit() {
        return lower_limit;
    }

    public void setLower_limit(String lower_limit) {
        this.lower_limit = lower_limit;
    }

    public String getUpper_limit() {
        return upper_limit;
    }

    public void setUpper_limit(String upper_limit) {
        this.upper_limit = upper_limit;
    }

    public String getDiscount_or_amount() {
        return discount_or_amount;
    }

    public void setDiscount_or_amount(String discount_or_amount) {
        this.discount_or_amount = discount_or_amount;
    }

    public String getCoupon_body() {
        return coupon_body;
    }

    public void setCoupon_body(String coupon_body) {
        this.coupon_body = coupon_body;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}