package com.cess.gargotte.core.model.products;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Guillaume on 15/02/2017.
 */
public interface IProduct extends Serializable{

    public String getName();
    public double getPrice();
    public int getAmountSold();
    public int getAmountRemaining();
    public String getCat();

    public void applySale(int amount);
    public void removeAmount(int amount);
    public void addAmount(int amount);

    public String getRepresentation(int level);
    public String getRepresentation(int level, boolean simplifiedRepresentation);
    public boolean isComposedOf(IProduct product);
    public boolean isSameProduct(IProduct product);
}
