package com.cess.gargotte.core.model.products;

/**
 * Created by Guillaume on 15/02/2017.
 */
public class SimpleProduct implements IProduct {

    private final String name, category;
    private final double price;
    private int amountRemaining, amountSold;

    public SimpleProduct(String name, String category, double price, int amountRemaining, int amountSold){
        this.name = name;
        this.category = category;
        this.price = price;
        this.amountRemaining = amountRemaining;
        this.amountSold = amountSold;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getAmountSold() {
        return amountSold;
    }

    public int getAmountRemaining() {
        return amountRemaining;
    }

    public String getCat() {
        return category;
    }
}
