package com.cess.gargotte.core.model.products;

/**
 * Created by Guillaume on 15/02/2017.
 */
public class SimpleProduct implements IProduct {

    private static final long serialVersionUID = 1L;

    private final String name, category;
    private final double price;
    private int amountRemaining, amountSold;

    public SimpleProduct(String name, String category, double price, int amountRemaining, int amountSold){
        this.name = name;
        this.category = category;
        this.price = price;
        this.amountRemaining = amountRemaining;
        this.amountSold = amountSold;
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
        return amountRemaining;
    }

    public String getCat() {
        return category;
    }

    public void applySale(int amount) {
        removeAmount(amount);
        this.amountSold+=amount;
    }

    public void removeAmount(int amount) {
        this.amountRemaining-=amount;
        if(amountRemaining<0)
            throw new IllegalStateException();
    }

    public void addAmount(int amount) {
        this.amountRemaining+=amount;
    }

    public String getRepresentation(int level){
        StringBuffer sb = new StringBuffer();
        for(int i =0; i<level;i++){
            sb.append("\t");
        }

        sb.append(name).append("\t");
        sb.append(category).append("\t");
        sb.append(price).append("\t");
        sb.append(amountRemaining).append("\t");
        sb.append(amountSold);

        return sb.toString();
    }
    
    @Override
    public boolean isComposedOf (IProduct product) {
        return this.isSameProduct(product);
    }
    
    public boolean isSameProduct (IProduct product) {
        if(product.getClass().equals(SimpleProduct.class)){
            return this.name.equals(product.getName()) && this.category.equals(product.getCat()) && this.price==product.getPrice();
        }else{
            return false;
        }
    }
}
