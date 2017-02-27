package com.cess.gargotte.gui.modules.sale_viewer.model;

import com.cess.gargotte.core.model.products.IProduct;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.List;

/**
 * Created by Guillaume on 25/02/2017.
 */
public class ObservableSaleLog implements IObservableLog{
    
    private IProduct product;
    private int amount;
    private double totalPrice;
    
    public ObservableSaleLog(IProduct product, int amount, double totalPrice){
        this.product = product;
        this.amount = amount;
        this.totalPrice = totalPrice;
    }
    
    @Override
    public StringProperty name () {
        return new SimpleStringProperty(product.getName());
    }
    
    @Override
    public StringProperty timeStamp () {
        return new SimpleStringProperty("");
    }
    
    @Override
    public StringProperty amount () {
        return new SimpleStringProperty(Integer.toString(amount));
    }
    
    @Override
    public StringProperty price () {
        return new SimpleStringProperty(Double.toString(totalPrice)+"€");
    }
    
    @Override
    public StringProperty paymentMethod () {
        return new SimpleStringProperty("");
    }
    
    @Override
    public List<IObservableLog> content () {
        return null;
    }
}
