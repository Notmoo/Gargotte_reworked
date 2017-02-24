package com.cess.gargotte.core.model.sales;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guillaume on 20/02/2017.
 */
public class Order {

    private PaymentMethod paymentMethod;
    private List<Sale> sales;

    public Order(List<Sale> sales, PaymentMethod paymentMethod){
        this.sales = new ArrayList<>();
        this.paymentMethod = paymentMethod;
        
        if(sales!=null){
            this.sales.addAll(sales);
        }
    }

    public List<Sale> getSales() {
        return sales;
    }
    public PaymentMethod getPaymentMethod ( ) {
        return paymentMethod;
    }
}
