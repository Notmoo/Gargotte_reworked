package com.cess.gargotte.core.model.products;

/**
 * Created by Guillaume on 15/02/2017.
 */
public interface IProduct {

    public String getName();
    public double getPrice();
    public int getAmountSold();
    public int getAmountRemaining();
    public String getCat();
}
