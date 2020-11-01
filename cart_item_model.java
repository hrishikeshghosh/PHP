package com.example.ecommerce_03;

import java.util.ArrayList;
import java.util.List;

public class cart_item_model {
    public static final int CART_ITEM = 0;
    public static final int TOTAL_AMOUNT = 1;

    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    ////////cart item
    private String product_id;
    private String product_image;
    private String product_title;
    private long free_coupons;
    private String product_price;
    private String cutted_price;
    private long product_quantity;
    private long product_max_quantity;
    private long product_stock_quantity;
    private long offers_applied;
    private long coupons_applied;
    private boolean in_stock;
    private List<String> quantity_IDs;
    private boolean qty_error;
    private String selected_coupon_id;
    private String discounted_price;
    private boolean COD;

    ////////cart item


    public cart_item_model(boolean COD,int type, String product_id, String product_image, String product_title, long free_coupons, String product_price, String cutted_price, long product_quantity, long offers_applied, long coupons_applied, boolean in_stock, long product_max_quantity, long product_stock_quantity) {
        this.type = type;
        this.product_id = product_id;
        this.product_image = product_image;
        this.product_title = product_title;
        this.free_coupons = free_coupons;
        this.product_price = product_price;
        this.cutted_price = cutted_price;
        this.product_quantity = product_quantity;
        this.offers_applied = offers_applied;
        this.coupons_applied = coupons_applied;
        this.product_max_quantity = product_max_quantity;
        this.in_stock = in_stock;
        quantity_IDs = new ArrayList<>();
        this.product_stock_quantity = product_stock_quantity;
        qty_error = false;
        this.COD=COD;
    }

    public boolean isCOD() {
        return COD;
    }

    public void setCOD(boolean COD) {
        this.COD = COD;
    }

    public String getDiscounted_price() {
        return discounted_price;
    }

    public void setDiscounted_price(String discounted_price) {
        this.discounted_price = discounted_price;
    }

    public String getSelected_coupon_id() {
        return selected_coupon_id;
    }

    public void setSelected_coupon_id(String selected_coupon_id) {
        this.selected_coupon_id = selected_coupon_id;
    }

    public boolean isQty_error() {
        return qty_error;
    }

    public void setQty_error(boolean qty_error) {
        this.qty_error = qty_error;
    }

    public long getProduct_stock_quantity() {
        return product_stock_quantity;
    }

    public void setProduct_stock_quantity(long product_stock_quantity) {
        this.product_stock_quantity = product_stock_quantity;
    }

    public List<String> getQuantity_IDs() {
        return quantity_IDs;
    }

    public void setQuantity_IDs(List<String> quantity_IDs) {
        this.quantity_IDs = quantity_IDs;
    }

    public long getProduct_max_quantity() {
        return product_max_quantity;
    }

    public void setProduct_max_quantity(long product_max_quantity) {
        this.product_max_quantity = product_max_quantity;
    }

    public boolean isIn_stock() {
        return in_stock;
    }

    public void setIn_stock(boolean in_stock) {
        this.in_stock = in_stock;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public static int getCartItem() {
        return CART_ITEM;
    }

    public static int getTotalAmount() {
        return TOTAL_AMOUNT;
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

    public long getFree_coupons() {
        return free_coupons;
    }

    public void setFree_coupons(long free_coupons) {
        this.free_coupons = free_coupons;
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

    public long getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(long product_quantity) {
        this.product_quantity = product_quantity;
    }

    public long getOffers_applied() {
        return offers_applied;
    }

    public void setOffers_applied(long offers_applied) {
        this.offers_applied = offers_applied;
    }

    public long getCoupons_applied() {
        return coupons_applied;
    }

    public void setCoupons_applied(long coupons_applied) {
        this.coupons_applied = coupons_applied;
    }


    //////////cart total

    private int total_items, totalItems_price, total_amount, savedAmount;
    private String delivery_price;

    public cart_item_model(int type) {

        this.type = type;
    }

    public int getTotal_items() {
        return total_items;
    }

    public void setTotal_items(int total_items) {
        this.total_items = total_items;
    }

    public int getTotalItems_price() {
        return totalItems_price;
    }

    public void setTotalItems_price(int totalItems_price) {
        this.totalItems_price = totalItems_price;
    }

    public int getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(int total_amount) {
        this.total_amount = total_amount;
    }

    public int getSavedAmount() {
        return savedAmount;
    }

    public void setSavedAmount(int savedAmount) {
        this.savedAmount = savedAmount;
    }

    public String getDelivery_price() {
        return delivery_price;
    }

    public void setDelivery_price(String delivery_price) {
        this.delivery_price = delivery_price;
    }
    //////////cart total
}

