package com.example.copup2.Models.PostModels.ProductType;


public class Shoes extends ProductType {
    private double size;
    private String type = "Shoes";

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Shoes(double size, String type) {
        this.size = size;
        this.type = type;
    }

    public Shoes(double size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "Shoes{" +
                "size=" + size +
                '}';
    }

    public Shoes() {

    }

    public double getSize() {
        return size;
    }


    public void setSize(double size) {
        this.size = size;
    }
}
