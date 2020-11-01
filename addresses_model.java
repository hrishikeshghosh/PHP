package com.example.ecommerce_03;

public class addresses_model {

    private Boolean selected;
    private String city;
    private String locality;
    private String flat_no;
    private String pincode;
    private String landmark;
    private String name;
    private String mobile_no;
    private String alternate_mobile_no;
    private String state;

    public addresses_model(Boolean selected, String city, String locality, String flat_no, String pincode, String landmark, String name, String mobile_no, String alternate_mobile_no, String state) {
        this.selected = selected;
        this.city = city;
        this.locality = locality;
        this.flat_no = flat_no;
        this.pincode = pincode;
        this.landmark = landmark;
        this.name = name;
        this.mobile_no = mobile_no;
        this.alternate_mobile_no = alternate_mobile_no;
        this.state = state;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getFlat_no() {
        return flat_no;
    }

    public void setFlat_no(String flat_no) {
        this.flat_no = flat_no;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getAlternate_mobile_no() {
        return alternate_mobile_no;
    }

    public void setAlternate_mobile_no(String alternate_mobile_no) {
        this.alternate_mobile_no = alternate_mobile_no;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}