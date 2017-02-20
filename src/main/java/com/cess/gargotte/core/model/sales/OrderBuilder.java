package com.cess.gargotte.core.model.sales;

import com.cess.gargotte.core.model.products.IProduct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Guillaume on 19/02/2017.
 */
public class OrderBuilder {

    Map<IProduct, Integer> content;

    OrderBuilder(){
        content = new HashMap<IProduct, Integer>();
    }

    public void addProduct(IProduct product){
        if(this.content.containsKey(product)){
            this.content.put(product,this.content.get(product)+1);
        }else{
            this.content.put(product, 1);
        }
    }

    public void removeProduct(IProduct product){
        if(this.content.containsKey(product)){
            if(this.content.get(product)==1){
                this.content.remove(product);
            }else{
                this.content.put(product, this.content.get(product)-1);
            }
        }
    }

    public Order build(){
        List<Sale> sales = new ArrayList<Sale>();
        for(IProduct product : content.keySet()){
            sales.add(new Sale(product, this.content.get(product)));
        }
        return new Order(sales);
    }
}
