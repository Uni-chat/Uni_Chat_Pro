package com.leonard.unichat;

public class NoticeModel {

    private String date,img, name, text;

    public NoticeModel() {

    }

    public NoticeModel(String date, String img, String name, String text) {
        this.date = date;
        this.img = img;
        this.name = name;
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
