package com.example.ecommerce_03;

public class horizontal_scroll_product_model {
    private String product_id;
    private String product_image;
    private String product_title;
    private String product_description;
    private String product_price;

    public horizontal_scroll_product_model(String product_id,String product_image, String product_title, String product_description, String product_price) {
        this.product_id=product_id;
        this.product_image = product_image;
        this.product_title = product_title;
        this.product_description = product_description;
        this.product_price = product_price;
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

    public String getProduct_description() {
        return product_description;
    }

    public void setProduct_description(String product_description) {
        this.product_description = product_description;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }
}
