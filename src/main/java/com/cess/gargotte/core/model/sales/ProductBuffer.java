package com.cess.gargotte.core.model.sales;

import com.cess.gargotte.core.model.products.ComposedProduct;
import com.cess.gargotte.core.model.products.IProduct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Guillaume on 19/02/2017.
 */
public class ProductBuffer {

    private Map<IProduct, Integer> content;

    public ProductBuffer(){
        content = new HashMap<>();
    }
    
    public boolean addProduct(IProduct product){
        boolean reply = false;
        if(this.getRemainingAmount(product)>0) {
            if ( this.content.containsKey(product) ) {
                this.content.put(product, this.content.get(product) + 1);
                reply = true;
            } else {
                this.content.put(product, 1);
                reply = true;
            }
        }
    
        return reply;
    }
    
    /**
     * Renvoie le nombre de fois que le produit donné apparait dans le buffer. Ce nombre prend en compte le
     * fait que le produit peut être un composant d'un autre produit contenu dans le buffer.
     *
     * @param product Produit à utiliser pour le compte
     * @return Nombre d'éléments dénombrés
     */
    private int getBufferedAmount (IProduct product) {
        int amount = 0;
        for(IProduct currProduct : this.content.keySet()){
            if(currProduct.isComposedOf(product))
                amount+=content.get(currProduct);
        }
        return amount;
    }
    
    /**
     * Renvoie le nombre de produits disponibles à la vente en prenant en compte le
     * fait que certains composants sont potentiellements aussi dans le buffer.
     *
     * @param product Produit à utiliser pour le compte
     * @return Nombre d'éléments dénombrés
     */
    private int getRemainingAmount (IProduct product) {
        int remainingAmount = product.getAmountRemaining()- getBufferedAmount(product);
        
        if(product.getClass().equals(ComposedProduct.class)){
            ComposedProduct cp = (ComposedProduct) product;
            for(IProduct component : cp.getComponents()){
                remainingAmount = Math.min(remainingAmount, getRemainingAmount(component));
            }
        }
        
        return remainingAmount;
    }
    
    public boolean removeProduct(IProduct product){
        if(this.content.containsKey(product)){
            if(this.content.get(product)==1){
                this.content.remove(product);
            }else{
                this.content.put(product, this.content.get(product)-1);
            }
            return true;
        }
        return false;
    }

    public Order makeOrder(){
        return makeOrder(null);
    }
    
    public Order makeOrder(PaymentMethod paymentMethod){
        List<Sale> sales = new ArrayList<>();
        for(IProduct product : content.keySet()){
            sales.add(new Sale(product, this.content.get(product)));
        }
        return new Order(sales, paymentMethod);
    }
}
