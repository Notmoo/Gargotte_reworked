package com.cess.gargotte.core.model.products;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guillaume on 15/02/2017.
 */
public class ComposedProduct implements IProduct {

    private final String name;
    private final double price;
    private int amountSold;
    private final List<IProduct> components;

    public ComposedProduct(String name, double price, int amountSold, List<IProduct> components){
        this.name = name;
        this.price = price;
        this.amountSold = amountSold;
        this.components = new ArrayList<IProduct>();
        if(components!=null){
            this.components.addAll(components);
        }
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getAmountSold() {
        return amountSold;
    }

    public int getAmountRemaining() {
        if(components.size()==0)
            return 0;
        else{
            int reply = this.components.get(0).getAmountRemaining();
            for(int i = 1; i<this.components.size(); i++){
                reply = Math.min(reply, this.components.get(i).getAmountRemaining());
            }
            return reply;
        }
    }

    public String getCat() {
        return "ComposedProduct";
    }

    public List<IProduct> getComponents(){
        return components;
    }

    public void applySale(int amount) {
        this.removeAmount(amount);
        this.amountSold+=amount;
    }

    public void removeAmount(int amount){
        for(IProduct component : components){
            component.removeAmount(amount);
        }
    }

    public void addAmount(int amount){
        for(IProduct component : components){
            component.addAmount(amount);
        }
    }
}
