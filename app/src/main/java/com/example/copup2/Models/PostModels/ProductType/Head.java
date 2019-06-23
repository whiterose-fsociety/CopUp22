package com.example.copup2.Models.PostModels.ProductType;

public class Head extends ProductType {
    private String size;
    private String type = "Head";

    public Head(String size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "Head{" +
                "size='" + size + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Head() {
    }

    public Head(String size, String type) {
        this.size = size;
        this.type = type;
    }
}
