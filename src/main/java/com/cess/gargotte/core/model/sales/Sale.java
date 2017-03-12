package com.cess.gargotte.core.model.sales;

import com.cess.gargotte.core.model.products.IReadOnlyProduct;

/**
 * Created by Guillaume on 20/02/2017.
 */
public class Sale {

    private final IReadOnlyProduct product;
    private final int amount;

    public Sale(IReadOnlyProduct product, int amount){
        this.product = product;
        this.amount = amount;
    }

    public IReadOnlyProduct getProduct() {
        return product;
    }

    public int getAmount() {
        return amount;
    }
    
    public double getPrice(){return product.getPrice()*amount;}
}
