package com.example.gilbertecommerce.Entity;

public class Tag {

    private int tagId;
    private String tagValue;
    private String brand;
    private int category;

    public Tag() {
    }

    public Tag(String tagValue) {
        this.tagValue = tagValue;
    }
    public Tag(int id, String tagValue) {
        this.tagId = id;
        this.tagValue = tagValue;
    }

    public String getTagValue() {
        return tagValue;
    }

    public void setTagValue(String tagValue) {
        this.tagValue = tagValue;
    }

    public int getId() {
        return tagId;
    }

    public void setId(int id) {
        this.tagId = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return tagValue;
    }

}