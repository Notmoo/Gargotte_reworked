package com.cess.gargotte.core.model.products;

import java.io.Serializable;

/**
 * Created by Guillaume on 28/02/2017.
 */
public interface IProduct extends Serializable{
    String getName();
    double getPrice();
    int getAmountSold();
    int getAmountRemaining();
    String getCat();
    
    void applySale(int amount);
    void removeAmount(int amount);
    void addAmount(int amount);
    void removeComponent(IProduct toRemove);
    void replaceComponent (IProduct toReplace, IProduct with);
    
    String getRepresentation(int level);
    String getRepresentation(int level, boolean simplifiedRepresentation);
    boolean isComposedOf(IProduct product);
    boolean isSameProduct(IProduct product);
}
