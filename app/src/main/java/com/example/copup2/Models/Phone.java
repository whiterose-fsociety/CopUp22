package com.example.copup2.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Phone extends BaseUserClass implements Parcelable {
    private String code;
    private String verificationCode;
    private String number;
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Phone(String code, String verificationCode, String number, String password) {
        this.code = code;
        this.verificationCode = verificationCode;
        this.number = number;
        this.password = password;
    }

    public Phone(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public static Creator<Phone> getCREATOR() {
        return CREATOR;
    }

    public static void setCREATOR(Creator<Phone> CREATOR) {
        Phone.CREATOR = CREATOR;
    }

    public Phone(String code, String verificationCode, String number) {
        this.code = code;
        this.verificationCode = verificationCode;
        this.number = number;
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
        number = parcel.readString();
        password = parcel.readString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "Phone{" +
                "code='" + code + '\'' +
                ", verificationCode='" + verificationCode + '\'' +
                ", number='" + number + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(code);
        parcel.writeString(verificationCode);
        parcel.writeString(number);
        parcel.writeString(password);
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
