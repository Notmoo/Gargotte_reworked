package com.cess.gargotte.core.model.products;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guillaume on 15/02/2017.
 */
public class ComposedProduct implements IProduct {

    private final String name;
    private final double price;
    private int amountSold, amountRemaining;
    private final List<IProduct> components;

    public ComposedProduct(String name, double price, int amountRemaining, int amountSold, List<IProduct> components){
        this.name = name;
        this.price = price;
        this.amountRemaining = amountRemaining;
        this.amountSold = amountSold;
        this.components = new ArrayList<IProduct>();
        if(components!=null){
            this.components.addAll(components);
        }
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
        return "ComposedProduct";
    }

    public List<IProduct> getComponents(){
        return components;
    }
}
