package com.example.copup2.Models;

public class Following {
    private String userID;

    @Override
    public String toString() {
        return "Following{" +
                "userID='" + userID + '\'' +
                '}';
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Following(String userID) {
        this.userID = userID;
    }

    public Following() {
    }
}
