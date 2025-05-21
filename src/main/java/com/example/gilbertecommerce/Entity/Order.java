package com.example.gilbertecommerce.Entity;

public class Order {

    private int orderID;
    private double subTotal;
    //private double discount; Unclear method of implementation, to be resolved
    //Invoice invoice object, to be implemented

    public Order() {}

    public Order(int orderID, double subTotal) {
        this.orderID = orderID;
        this.subTotal = subTotal;

    }



}
