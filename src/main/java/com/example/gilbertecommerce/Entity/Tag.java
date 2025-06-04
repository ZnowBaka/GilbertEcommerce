package com.example.gilbertecommerce.Entity;

public class Tag {

    private int id;
    private String tagValue;

    public Tag() {
    }

    public Tag(String tagValue) {
        this.tagValue = tagValue;
    }
    public Tag(int id, String tagValue) {
        this.id = id;
        this.tagValue = tagValue;
    }

    public String getTagValue() {
        return tagValue;
    }

    public void setTagValue(String tagValue) {
        this.tagValue = tagValue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return tagValue;
    }

}