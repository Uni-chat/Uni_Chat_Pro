package com.leonard.unichat;

public class UsersModel {

    private String NAME, ID, IMG_URL, DEPT;


    public UsersModel() {

    }

    public UsersModel(String NAME, String ID, String IMG_URL, String DEPT ) {
        this.NAME = NAME;
        this.ID = ID;
        this.IMG_URL = IMG_URL;
        this.DEPT = DEPT;
    }



    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getIMG_URL() {
        return IMG_URL;
    }

    public void setIMG_URL(String IMG_URL) {
        this.IMG_URL = IMG_URL;
    }

    public String getDEPT() {
        return DEPT;
    }

    public void setDEPT(String DEPT) {
        this.DEPT = DEPT;
    }
}



