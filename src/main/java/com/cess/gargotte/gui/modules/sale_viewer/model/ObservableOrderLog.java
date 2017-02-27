package com.cess.gargotte.gui.modules.sale_viewer.model;

import com.cess.gargotte.core.model.sales.PaymentMethod;
import javafx.beans.property.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guillaume on 25/02/2017.
 */
public class ObservableOrderLog implements IObservableLog{
    
    private List<IObservableLog> sales;
    private String timestamp;
    private double totalPrice;
    private PaymentMethod pm;
    
    public ObservableOrderLog(List<IObservableLog> sales, String timeStamp, double totalPrice, PaymentMethod pm){
        this.sales = new ArrayList<>();
        if(sales!=null)
            this.sales.addAll(sales);
        this.timestamp = timeStamp;
        this.totalPrice = totalPrice;
        this.pm = pm;
    }
    
    @Override
    public List<IObservableLog> content () {
        return sales;
    }
    
    @Override
    public StringProperty name () {
        return new SimpleStringProperty("");
    }
    
    @Override
    public StringProperty timeStamp () {
        return new SimpleStringProperty(timestamp);
    }
    
    @Override
    public StringProperty amount () {
        return new SimpleStringProperty("");
    }
    
    @Override
    public StringProperty price () {
        return new SimpleStringProperty(Double.toString(totalPrice)+"€");
    }
    
    @Override
    public StringProperty paymentMethod () {
        return new SimpleStringProperty(pm.getText());
    }
}
