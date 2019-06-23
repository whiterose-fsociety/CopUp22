package com.example.copup2.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Phone implements Parcelable {
    private String code;
    private String verificationCode;

    @Override
    public String toString() {
        return "Phone{" +
                "code='" + code + '\'' +
                ", verificationCode='" + verificationCode + '\'' +
                '}';
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public Phone(String code, String verificationCode) {
        this.code = code;
        this.verificationCode = verificationCode;
    }

    public Phone() {
    }

    protected Phone(Parcel parcel) {
        code = parcel.readString();
        verificationCode = parcel.readString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(code);
        parcel.writeString(verificationCode);
    }

    public static Creator<Phone> CREATOR = new Creator<Phone>() {
        @Override
        public Phone createFromParcel(Parcel parcel) {
            return new Phone(parcel);
        }

        @Override
        public Phone[] newArray(int i) {
            return new Phone[i];
        }
    };

}
