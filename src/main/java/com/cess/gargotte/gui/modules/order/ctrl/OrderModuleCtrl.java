package com.cess.gargotte.gui.modules.order.ctrl;

import com.cess.gargotte.core.model.GargotteModel;
import com.cess.gargotte.core.model.products.IProduct;
import com.cess.gargotte.core.model.sales.Sale;
import com.cess.gargotte.gui.modules.order.view.OrderModuleView;
import javafx.collections.ObservableList;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.util.List;

/**
 * Created by Guillaume on 21/02/2017.
 */
public class OrderModuleCtrl {
    
    private final GargotteModel model;
    private OrderModuleView view;
    
    public OrderModuleCtrl (GargotteModel model) {
        if(model == null){
            throw new NullPointerException();
        }
        this.model = model;
    }
    
    public void setView (OrderModuleView view) {
        this.view = view;
    }
    
    public List<String> getCategories ( ) {
        return model.getCatList();
    }
    
    public List<IProduct> getProducts (String category) {
        return this.model.getProductsFromCat(category);
    }
    
    public List<Sale> getOrderSales ( ) {
        return model.getCurrentOrder().getSales();
    }
}
