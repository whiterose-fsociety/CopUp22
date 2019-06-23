package com.example.copup2.Models.PostModels.ProductType;


public class LowerBody extends ProductType{
    private String size;
    private String type = "Lower Body";

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LowerBody(String size, String type) {
        this.size = size;
        this.type = type;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public LowerBody() {
    }

    @Override
    public String toString() {
        return "LowerBody{" +
                "size='" + size + '\'' +
                '}';
    }



    public LowerBody(String size) {
        this.size = size;
    }
}

