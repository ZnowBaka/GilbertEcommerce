package com.example.gilbertecommerce.Entity;

import jakarta.annotation.PostConstruct;

import java.util.HashMap;
import java.util.Map;

public class TagInsertForm {
    private String gender;
    private String designer;
    private String home;
    private String beauty;
    private String brand;
    private String condition;
    private String clothing;
    private String accessories;
    private String shoes;
    private String bags_and_luggage;
    private String jewelry;
    private String international_size;
    private String shoe_size;

    private Map<String, String> tagSelections = new HashMap<>();

    public TagInsertForm() {}
    public TagInsertForm(String gender, String designer, String home, String beauty, String brand, String condition, String clothing, String accessories, String shoes, String bags_and_luggage, String jewelry, String international_size, String shoe_size) {
        this.gender = gender;
        this.designer = designer;
        this.home = home;
        this.beauty = beauty;
        this.brand = brand;
        this.condition = condition;
        this.clothing = clothing;
        this.accessories = accessories;
        this.shoes = shoes;
        this.bags_and_luggage = bags_and_luggage;
        this.jewelry = jewelry;
        this.international_size = international_size;
        this.shoe_size = shoe_size;
    }

    @PostConstruct
    public void initializeTagSelections() {
        tagSelections.put("gender", gender);
        tagSelections.put("designer", designer);
        tagSelections.put("home", home);
        tagSelections.put("beauty", beauty);
        tagSelections.put("brand", brand);
        tagSelections.put("condition", condition);
        tagSelections.put("clothing", clothing);
        tagSelections.put("accessories", accessories);
        tagSelections.put("shoes", shoes);
        tagSelections.put("bags_and_luggage", bags_and_luggage);
        tagSelections.put("jewelry", jewelry);
        tagSelections.put("international_size", international_size);
        tagSelections.put("shoe_size", shoe_size);
    }


    public Map<String, String> getTagSelections() {
        return tagSelections;
    }

    public void setTagSelections(Map<String, String> tagSelections) {
        this.tagSelections = tagSelections;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDesigner() {
        return designer;
    }

    public void setDesigner(String designer) {
        this.designer = designer;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getBeauty() {
        return beauty;
    }

    public void setBeauty(String beauty) {
        this.beauty = beauty;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getClothing() {
        return clothing;
    }

    public void setClothing(String clothing) {
        this.clothing = clothing;
    }

    public String getAccessories() {
        return accessories;
    }

    public void setAccessories(String accessories) {
        this.accessories = accessories;
    }

    public String getShoes() {
        return shoes;
    }

    public void setShoes(String shoes) {
        this.shoes = shoes;
    }

    public String getBags_and_luggage() {
        return bags_and_luggage;
    }

    public void setBags_and_luggage(String bags_and_luggage) {
        this.bags_and_luggage = bags_and_luggage;
    }

    public String getJewelry() {
        return jewelry;
    }

    public void setJewelry(String jewelry) {
        this.jewelry = jewelry;
    }

    public String getInternational_size() {
        return international_size;
    }

    public void setInternational_size(String international_size) {
        this.international_size = international_size;
    }

    public String getShoe_size() {
        return shoe_size;
    }

    public void setShoe_size(String shoe_size) {
        this.shoe_size = shoe_size;
    }
}

