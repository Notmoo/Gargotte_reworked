package com.cess.gargotte.core.model.products;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guillaume on 28/02/2017.
 */
public class ComposedReadOnlyProduct implements IReadOnlyProduct {
    
    private static final long serialVersionUID = 1L;
    
    private final String name;
    private final String category;
    private final double price;
    private int amountSold;
    private final List<IReadOnlyProduct> components;
    
    public ComposedReadOnlyProduct(String name, String category, double price, int amountSold, List<IReadOnlyProduct> components){
        this.name = name;
        this.category = category;
        this.price = price;
        this.amountSold = amountSold;
        this.components = new ArrayList<>();
        if(components!=null){
            this.components.addAll(components);
        }
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public double getPrice() {
        return price;
    }
    
    @Override
    public int getAmountSold() {
        return amountSold;
    }
    
    @Override
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
    
    @Override
    public String getCat() {
        return category;
    }
    
    public List<IReadOnlyProduct> getComponents(){
        return components;
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
            for (IReadOnlyProduct component : components) {
                sb.append(component.getRepresentation(level + 1, simplifiedRepresentation)).append("\n");
            }
        }
        return sb.toString();
    }
    
    @Override
    public boolean isComposedOf (IReadOnlyProduct product) {
        if(isSameProduct(product)){
            return true;
        }else{
            for(IReadOnlyProduct component : components){
                if(component.isComposedOf(product))
                    return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean isSameProduct (IReadOnlyProduct product) {
        if(product.getClass().equals(ComposedReadOnlyProduct.class)){
            ComposedReadOnlyProduct cp = (ComposedReadOnlyProduct) product;
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
