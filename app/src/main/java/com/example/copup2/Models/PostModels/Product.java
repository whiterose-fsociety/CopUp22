package com.example.copup2.Models.PostModels;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ProgressBar;

import com.example.copup2.Models.PostModels.ProductType.ProductType;

public class Product implements Parcelable {
    @Override
    public String toString() {
        return "Product{" +
                "image='" + image + '\'' +
                ", description='" + description + '\'' +
                ", title='" + title + '\'' +
                ", location='" + location + '\'' +
                ", gender='" + gender + '\'' +
                ", productType=" + productType +
                ", condition='" + condition + '\'' +
                ", transactionType='" + transactionType + '\'' +
                ", price=" + price +
                '}';
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public static void setCREATOR(Creator<Product> CREATOR) {
        Product.CREATOR = CREATOR;
    }

    private String image;
    private String description;
    private String title;
    private String location;
    private String gender;
    private ProductType productType;
    private String condition;
    private String transactionType;
    private String userID;

    public String getTransactionType() {
        return transactionType;
    }

    public static Creator<Product> getCREATOR() {
        return CREATOR;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    private double price;

    public Product() {
    }

    protected Product(Parcel parcel) {
        image = parcel.readString();
        description = parcel.readString();
        title = parcel.readString();
        location = parcel.readString();
        gender = parcel.readString();
        productType = parcel.readParcelable(ProductType.class.getClassLoader());
        condition = parcel.readString();
        transactionType = parcel.readString();
        price = parcel.readDouble();
        userID = parcel.readString();

    }

    public String getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public String getGender() {
        return gender;
    }

    public ProductType getProductType() {
        return productType;
    }

    public String getCondition() {
        return condition;
    }

    public double getPrice() {
        return price;
    }

    private Product(Builder builder) {
        this.image = builder.image;
        this.description = builder.description;
        this.title = builder.title;
        this.location = builder.location;
        this.gender = builder.gender;
        this.productType = builder.productType;
        this.condition = builder.condition;
        this.transactionType = builder.transactionType;
        this.price = builder.price;
        this.userID = builder.userID;
    }

    public static class Builder {
        private String image;
        private String description;
        private String title;
        private String location;
        private String gender;
        private ProductType productType;
        private String condition;
        private String transactionType;
        private double price;
        private String userID;

        public Builder(String image) {
            this.image = image;
        }

        public Builder hasDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder hasTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder hasLocation(String location) {
            this.location = location;
            return this;
        }

        public Builder hasUserID(String userID) {
            this.userID = userID;
            return this;
        }

        public Builder hasGender(String gender) {
            this.gender = gender;
            return this;
        }

        public Builder hasProductType(ProductType productType) {
            this.productType = productType;
            return this;
        }

        public Builder hasCondition(String condition) {
            this.condition = condition;
            return this;
        }

        public Builder hasPrice(double price) {
            this.price = price;
            return this;
        }

        public Builder hasTransaction(String transactionType) {
            this.transactionType = transactionType;
            return this;
        }

        public String getImage() {
            return image;
        }

        public String getDescription() {
            return description;
        }

        public String getTitle() {
            return title;
        }

        public String getLocation() {
            return location;
        }

        public String getGender() {
            return gender;
        }

        public ProductType getProductType() {
            return productType;
        }

        public String getCondition() {
            return condition;
        }

        public String getTransactionType() {
            return transactionType;
        }

        public double getPrice() {
            return price;
        }

        public String getUserID() {
            return userID;
        }

        public Product build() {
            //call the private constructor in the outer class
            return new Product(this);
        }


    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(image);
        parcel.writeString(description);
        parcel.writeString(title);
        parcel.writeString(location);
        parcel.writeString(gender);
        parcel.writeParcelable(productType, i);
        parcel.writeString(condition);
        parcel.writeString(transactionType);
        parcel.writeDouble(price);
        parcel.writeString(userID);
    }

    public static Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel parcel) {
            return new Product(parcel);
        }

        @Override
        public Product[] newArray(int i) {
            return new Product[i];
        }
    };

}
