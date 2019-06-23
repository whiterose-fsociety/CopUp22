package com.example.copup2.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class BaseUserClass implements Parcelable {
    private User user;
    private Phone phone;

    @Override
    public String toString() {
        return "BaseUserClass{" +
                "user=" + user +
                ", phone=" + phone +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        user.writeToParcel(parcel, i);
        phone.writeToParcel(parcel, i);
    }

    public BaseUserClass() {
    }

    public BaseUserClass(User user, Phone phone) {
        this.user = user;
        this.phone = phone;
    }

    protected BaseUserClass(Parcel parcel) {
        user = parcel.readParcelable(User.class.getClassLoader());
        phone = parcel.readParcelable(Phone.class.getClassLoader());
    }

    public static Creator<BaseUserClass> CREATOR = new Creator<BaseUserClass>() {
        @Override
        public BaseUserClass createFromParcel(Parcel parcel) {
            return new BaseUserClass(parcel);
        }

        @Override
        public User[] newArray(int i) {
            return new User[i];
        }
    };
}
