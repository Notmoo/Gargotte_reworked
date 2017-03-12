package com.cess.gargotte.core.model.products;

/**
 * Created by Guillaume on 28/02/2017.
 */
public class SimpleReadOnlyProduct implements IReadOnlyProduct {
    
    private static final long serialVersionUID = 1L;
    
    private final String name, category;
    private final double price;
    private int amountRemaining, amountSold;
    
    public SimpleReadOnlyProduct(String name, String category, double price, int amountRemaining, int amountSold){
        this.name = name;
        this.category = category;
        this.price = price;
        this.amountRemaining = amountRemaining;
        this.amountSold = amountSold;
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
        return amountRemaining;
    }
    
    @Override
    public String getCat() {
        return category;
    }
    
    @Override
    public String getRepresentation(int level){
        return getRepresentation(level, false);
    }
    
    @Override
    public String getRepresentation (int level, boolean simplifiedRepresentation) {
        StringBuilder sb = new StringBuilder();
        for(int i =0; i<level;i++){
            sb.append("\t");
        }
        
        sb.append(name).append("\t");
        sb.append(String.format("%.2f€", price)).append("\t");
        if(!simplifiedRepresentation) {
            sb.append(category).append("\t");
            sb.append(amountRemaining).append("\t");
            sb.append(amountSold);
        }
        
        return sb.toString();
    }
    
    @Override
    public boolean isComposedOf (IReadOnlyProduct product) {
        return this.isSameProduct(product);
    }
    
    @Override
    public boolean isSameProduct (IReadOnlyProduct product) {
        return product.getClass().equals(SimpleReadOnlyProduct.class)
               && this.name.equals(product.getName())
               && this.category.equals(product.getCat())
               && this.price==product.getPrice();
    }
}
