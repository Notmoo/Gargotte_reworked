package com.cess.gargotte.core.model.sales;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guillaume on 20/02/2017.
 */
public class Order {

    private List<Sale> sales;

    public Order(List<Sale> sales){
        this.sales = new ArrayList<Sale>();

        if(sales!=null){
            this.sales.addAll(sales);
        }
    }

    public List<Sale> getSales() {
        return sales;
    }
}
