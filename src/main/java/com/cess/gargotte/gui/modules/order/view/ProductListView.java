package com.cess.gargotte.gui.modules.order.view;

import com.cess.gargotte.core.model.products.IProduct;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;

/**
 * Created by Guillaume on 21/02/2017.
 */
public class ProductListView extends ListView<IProduct>{
    
    private final String catName;
    
    public ProductListView (String name){
        super();
        this.catName = name;
    }
    
    public String getCatName(){
        return catName;
    }
}
