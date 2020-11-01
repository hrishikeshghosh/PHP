package com.example.ecommerce_03;

import java.util.List;

public class home_page_model {
    public static final int BANNER_SLIDER=0;
    public static final int STRIP_AD_BANNER=1;
    public static final int HORIZONTAL_PRODUCT_VIEW=2;
    public  static  final int GRID_PRODUCT_VIEW=3;
    private int type;
    private String background_color;

    ////////////banner slider code
    private List<slider_model> slider_modelList;
    public home_page_model(int type, List<slider_model> slider_modelList) {
        this.type = type;
        this.slider_modelList = slider_modelList;
    }
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public List<slider_model> getSlider_modelList() {
        return slider_modelList;
    }
    public void setSlider_modelList(List<slider_model> slider_modelList) {
        this.slider_modelList = slider_modelList;
    }
////////////banner slider code

    ///////////strip ad code:

    private String resource;


    public home_page_model(int type, String resource, String background_color) {
        this.type = type;
        this.resource = resource;
        this.background_color = background_color;
    }
    public String getResource() {
        return resource;
    }
    public void setResource(String resource) {
        this.resource = resource;
    }
    public String getBackground_color() {
        return background_color;
    }
    public void setBackground_color(String background_color) {
        this.background_color = background_color;
    }


    ///////////strip ad code



    private String title;
    private List<horizontal_scroll_product_model>horizontalScrollProductModelList;
    private List<wishlist_model>viewall_productList;


    public home_page_model(int type, String title,String background_color, List<horizontal_scroll_product_model> horizontalScrollProductModelList,List<wishlist_model>viewall_productList) {
        this.type = type;
        this.title = title;
        this.horizontalScrollProductModelList = horizontalScrollProductModelList;
        this.background_color=background_color;
        this.viewall_productList=viewall_productList;
    }

    public List<wishlist_model> getViewall_productList() {
        return viewall_productList;
    }

    public void setViewall_productList(List<wishlist_model> viewall_productList) {
        this.viewall_productList = viewall_productList;
    }

    public home_page_model(int type, String title, String background_color, List<horizontal_scroll_product_model> horizontalScrollProductModelList) {
        this.type = type;
        this.title = title;
        this.horizontalScrollProductModelList = horizontalScrollProductModelList;
        this.background_color=background_color;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<horizontal_scroll_product_model> getHorizontalScrollProductModelList() {
        return horizontalScrollProductModelList;
    }

    public void setHorizontalScrollProductModelList(List<horizontal_scroll_product_model> horizontalScrollProductModelList) {
        this.horizontalScrollProductModelList = horizontalScrollProductModelList;
    }






}
