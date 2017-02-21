package com.cess.gargotte.core.model.sales;

import com.cess.gargotte.core.model.products.IProduct;

/**
 * Created by Guillaume on 20/02/2017.
 */
public class Sale {

    private final IProduct product;
    private final int amount;

    public Sale(IProduct product, int amount){
        this.product = product;
        this.amount = amount;
    }

    public IProduct getProduct() {
        return product;
    }

    public int getAmount() {
        return amount;
    }
}
