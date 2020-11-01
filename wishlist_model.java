package com.example.ecommerce_03;

import java.util.ArrayList;

public class wishlist_model {
    private String product_id;
    private String product_image;
    private String product_title;
    private long free_Coupon;
    private String rating;
    private long total_ratings;
    private String product_price;
    private String cutted_price;
    private boolean cod;
    private boolean instock;
    private ArrayList<String>tags;

    public wishlist_model(String product_id,String product_image, String product_title, long free_Coupon, String rating, long total_ratings, String product_price, String cutted_price, boolean cod,boolean instock) {
        this.product_id=product_id;
        this.product_image = product_image;
        this.product_title = product_title;
        this.free_Coupon = free_Coupon;
        this.rating = rating;
        this.total_ratings = total_ratings;
        this.product_price = product_price;
        this.cutted_price = cutted_price;
        this.instock=instock;
        this.cod = cod;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public boolean isInstock() {
        return instock;
    }

    public void setInstock(boolean instock) {
        this.instock = instock;
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

    public long getFree_Coupon() {
        return free_Coupon;
    }

    public void setFree_Coupon(long free_Coupon) {
        this.free_Coupon = free_Coupon;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public long getTotal_ratings() {
        return total_ratings;
    }

    public void setTotal_ratings(long total_ratings) {
        this.total_ratings = total_ratings;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getCutted_price() {
        return cutted_price;
    }

    public void setCutted_price(String cutted_price) {
        this.cutted_price = cutted_price;
    }

    public boolean isCod() {
        return cod;
    }

    public void setCod(boolean cod) {
        this.cod = cod;
    }
}
