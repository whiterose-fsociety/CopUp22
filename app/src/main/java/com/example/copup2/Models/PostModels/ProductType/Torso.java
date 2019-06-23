package com.example.copup2.Models.PostModels.ProductType;


public class Torso extends ProductType {
    private String size;
    private String type = "Torso";

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Torso(String size, String type) {
        this.size = size;
        this.type = type;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Torso() {
    }

    public Torso(String size) {
        this.size = size;
    }


    @Override
    public String toString() {
        return "Torso{" +
                "size='" + size + '\'' +
                '}';
    }


}
