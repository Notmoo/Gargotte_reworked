package com.cess.gargotte.core.model.products;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guillaume on 15/02/2017.
 */
public class ComposedProduct implements IProduct {

    private static final long serialVersionUID = 1L;

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

    public String getRepresentation(int level){
       return getRepresentation(level, false);
    }
    
    @Override
    public String getRepresentation (int level, boolean simplifiedRepresentation) {
        StringBuffer sb = new StringBuffer();
        for(int i =0; i<level;i++){
            sb.append("\t");
        }
    
        sb.append(name).append("\t");
        sb.append(String.format("%.2f€", price)).append("\t");
        if(!simplifiedRepresentation) {
            sb.append("Menu").append("\t");
            sb.append(this.getAmountRemaining( )).append("\t");
            sb.append(amountSold);
        }
        
        if(components.size()>0) {
            sb.append("\n");
            for (IProduct component : components) {
                sb.append(component.getRepresentation(level + 1, simplifiedRepresentation)).append("\n");
            }
        }
        return sb.toString();
    }
    
    @Override
    public boolean isComposedOf (IProduct product) {
        if(isSameProduct(product)){
            return true;
        }else{
            for(IProduct component : components){
                if(component.isComposedOf(product))
                    return true;
            }
        }
        return false;
    }
    
    public boolean isSameProduct (IProduct product) {
        if(product.getClass().equals(ComposedProduct.class)){
            ComposedProduct cp = (ComposedProduct) product;
            boolean state = this.name.equals(product.getName()) && this.price==product.getPrice();
            boolean components = this.components.size()==cp.components.size();
            if(components){
                for(int i = 0; i<this.components.size(); i++){
                    components = components && this.components.get(i).isSameProduct(cp.components.get(i));
                }
            }
            return state && components;
        }else{
            return false;
        }
    }
}
