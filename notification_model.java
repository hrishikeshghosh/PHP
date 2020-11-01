package com.example.ecommerce_03;

class notification_model{

    private String image,body;
    private boolean readed;

    public notification_model(String image, String body, boolean readed) {
        this.image = image;
        this.body = body;
        this.readed = readed;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isReaded() {
        return readed;
    }

    public void setReaded(boolean readed) {
        this.readed = readed;
    }
}
