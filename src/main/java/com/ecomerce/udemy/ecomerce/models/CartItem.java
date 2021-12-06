package com.ecomerce.udemy.ecomerce.models;

import lombok.Data;

@Data
public class CartItem {

    private int id;
    private String name;
    private double price;
    private int quantity;
    private String image;

    public CartItem(int id, String name, double price, int quantity, String image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
    }

}
