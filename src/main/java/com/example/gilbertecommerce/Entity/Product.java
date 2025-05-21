package com.example.gilbertecommerce.Entity;

public class Product {

    private int productID;
    private int sellerID;
    private String model;
    private String brand;
    private int genderType;
    private int articleType;

    public Product() {}

    public Product(int productID, String model, String brand, int genderType, int articleType) {
        this.productID = productID;
        this.model = model;
        this.brand = brand;
        this.genderType = genderType;
        this.articleType = articleType;
    }






}
