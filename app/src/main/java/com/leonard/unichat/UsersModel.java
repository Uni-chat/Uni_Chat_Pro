package com.leonard.unichat;

public class UsersModel {

    private int id;
    private String regID;
    private String dateOfBirth;



//    public UsersModel(int id, String regID, String dateOfBirth) {
//        this.id = id;
//        this.regID = regID;
//        this.dateOfBirth = dateOfBirth;
//    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRegID() {
        return regID;
    }

    public void setRegID(String regID) {
        this.regID = regID;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}



