package com.cess.gargotte.core.model.sales;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Guillaume on 20/02/2017.
 */
public class Order {

    private PaymentMethod paymentMethod;
    private List<Sale> sales;
    private String timeStamp;

    public Order(List<Sale> sales, PaymentMethod paymentMethod){
        this(sales, paymentMethod, new SimpleDateFormat("<E dd.MM.yy|HH:mm:ss>").format(new Date()));
    }
    
    public Order(List<Sale> sales, PaymentMethod paymentMethod, String timeStamp){
        this.sales = new ArrayList<>();
        this.paymentMethod = paymentMethod;
        this.timeStamp = timeStamp;
        
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
    public String getTimeStamp(){return timeStamp;}
    
    public Double getTotalPrice () {
        double price=0;
        for(Sale sale : sales){
            price+=sale.getProduct().getPrice()*sale.getAmount();
        }
        return price;
    }
}
