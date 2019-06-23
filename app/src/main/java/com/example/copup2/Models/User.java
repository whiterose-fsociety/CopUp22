package com.example.copup2.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class User implements Parcelable {
    private String email;
    private ArrayList<Followers> followers;
    private ArrayList<Following> following;
    private String profile_image;
    private String userID;
    private String username;
    private String number;
    private Phone phone;

    protected User(Parcel parcel) {
        email = parcel.readString();
        followers = parcel.readArrayList(null);
        following = parcel.readArrayList(null);
        profile_image = parcel.readString();
        userID = parcel.readString();
        username = parcel.readString();
        number = parcel.readString();
        phone = parcel.readParcelable(Phone.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(email);
        parcel.writeList(followers);
        parcel.writeList(following);
        parcel.writeString(profile_image);
        parcel.writeString(userID);
        parcel.writeString(username);
        parcel.writeString(number);
        parcel.writeParcelable(phone, i);
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", followers=" + followers +
                ", following=" + following +
                ", profile_image='" + profile_image + '\'' +
                ", userID='" + userID + '\'' +
                ", username='" + username + '\'' +
                ", number='" + number + '\'' +
                '}';
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<Followers> getFollowers() {
        return followers;
    }

    public void setFollowers(ArrayList<Followers> followers) {
        this.followers = followers;
    }

    public ArrayList<Following> getFollowing() {
        return following;
    }

    public void setFollowing(ArrayList<Following> following) {
        this.following = following;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public User(String email, ArrayList<Followers> followers, ArrayList<Following> following, String profile_image, String userID, String username, String number, Phone phone) {
        this.email = email;
        this.followers = followers;
        this.following = following;
        this.profile_image = profile_image;
        this.userID = userID;
        this.username = username;
        this.number = number;
        this.phone = phone;
    }

    /**
     * Nullify other entries
     */
    public User(String email, String userID, String username) {
        this.email = email;
        this.userID = userID;
        this.username = username;
    }

    public Phone getPhone() {
        return phone;
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }

    public User(String email, ArrayList<Followers> followers, ArrayList<Following> following, String profile_image, String userID, String username, String number) {
        this.email = email;
        this.followers = followers;
        this.following = following;
        this.profile_image = profile_image;
        this.userID = userID;
        this.username = username;
        this.number = number;
    }

    public User() {
    }

    public static Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel parcel) {
            return new User(parcel);
        }

        @Override
        public User[] newArray(int i) {
            return new User[i];
        }
    };
}
