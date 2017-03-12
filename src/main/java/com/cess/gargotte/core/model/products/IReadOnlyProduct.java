package com.cess.gargotte.core.model.products;

import java.io.Serializable;

/**
 * Created by Guillaume on 15/02/2017.
 */
public interface IReadOnlyProduct extends Serializable{

    String getName();
    double getPrice();
    int getAmountSold();
    int getAmountRemaining();
    String getCat();

    String getRepresentation(int level);
    String getRepresentation(int level, boolean simplifiedRepresentation);
    boolean isComposedOf(IReadOnlyProduct product);
    boolean isSameProduct(IReadOnlyProduct product);
}
