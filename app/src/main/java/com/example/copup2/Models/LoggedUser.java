package com.example.copup2.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class LoggedUser {
    private static LoggedUser loggedUser = new LoggedUser();
    private static User user;

    private LoggedUser() {
    }

    public static LoggedUser getInstance() {
        return loggedUser;
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        if (LoggedUser.getUser() == null) {
            LoggedUser.user = user;
        }
    }

    private LoggedUser(User user) {
        this.user = user;

    }


}
