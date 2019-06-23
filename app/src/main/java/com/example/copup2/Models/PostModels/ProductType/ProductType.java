package com.example.copup2.Models.PostModels.ProductType;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.copup2.Models.PostModels.Product;

public class ProductType implements Parcelable {
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }

    public ProductType() {
    }

    protected ProductType(Parcel parcel) {

    }

    public static Creator<ProductType> CREATOR = new Creator<ProductType>() {
        @Override
        public ProductType createFromParcel(Parcel parcel) {
            return new ProductType(parcel);
        }

        @Override
        public ProductType[] newArray(int i) {
            return new ProductType[i];
        }
    };
}
