package com.example.ecommerce_03;

public class slider_model {
    private String banner;
    private  String background_color;

    public slider_model(String banner, String background_color) {
        this.banner = banner;
        this.background_color = background_color;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getBackground_color() {
        return background_color;
    }

    public void setBackground_color(String background_color) {
        this.background_color = background_color;
    }
}
