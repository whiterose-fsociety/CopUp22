package com.example.copup2.Models.PostModels.ProductType;


public class Outfit extends ProductType {
    private String size;
    private String type = "Outfit";

    public String getSize() {
        return size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Outfit(String size, String type) {
        this.size = size;
        this.type = type;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Outfit() {

    }

    @Override
    public String toString() {
        return "Outfit{" +
                "size='" + size + '\'' +
                '}';
    }


    public Outfit(String size) {
        this.size = size;
    }

}
