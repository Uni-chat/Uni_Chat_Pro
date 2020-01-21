package com.leonard.unichat;

public class IconModel {

    private String  img;
    private String key;

    public IconModel () {

    }

    public IconModel(String img, String key) {
        this.img = img;
        this.key = key;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
