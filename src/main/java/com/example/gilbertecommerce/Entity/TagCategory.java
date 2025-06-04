package com.example.gilbertecommerce.Entity;

import java.util.Objects;

public class TagCategory {
    private int id;
    private String name;


    public TagCategory(){}

    public TagCategory(String name) {
        this.name = name;
    }

    public TagCategory(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // Needed to ensure proper key behavior in HashMap and Thymeleaf rendering
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TagCategory)) return false;
        TagCategory that = (TagCategory) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    // For rendering in Thymeleaf or debugging
    @Override
    public String toString() {
        return name;
    }
}