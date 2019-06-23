package com.example.copup2.Models;

public class Followers {
    private String userID;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Followers() {
    }

    @Override
    public String toString() {
        return "Followers{" +
                "userID='" + userID + '\'' +
                '}';
    }

    public Followers(String userID) {
        this.userID = userID;
    }
}
