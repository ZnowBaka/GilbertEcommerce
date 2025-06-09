package com.example.gilbertecommerce.Entity;

public class Tag {

    private int tagId;
    private String tagValue;

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

    @Override
    public String toString() {
        return tagValue;
    }

}