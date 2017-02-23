package com.cess.gargotte.gui.modules.stock.ctrl;

import com.cess.gargotte.core.model.IModel;
import com.cess.gargotte.core.model.products.IProduct;
import com.cess.gargotte.gui.modules.stock.popup.ProductEditionPopup;
import com.cess.gargotte.gui.modules.stock.view.StockModuleView;

import java.util.List;

/**
 * Created by Guillaume on 22/02/2017.
 */
public class StockModuleCtrl {
    
    private static final String PASSWORD = "licorne";
    
    private IModel model;
    private StockModuleView view;
    
    public StockModuleCtrl (IModel model) {
        this.model = model;
    }
    
    public void setView (StockModuleView view) {
        this.view = view;
    }
    
    public List<IProduct> getProducts ( ) {
        return model.getProducts();
    }
    
    public void onUnlockAttempt (String text) {
        if(text.equals(PASSWORD)){
            this.view.setViewUnlocked(true);
        }
    }
    
    public void onAddProductRequest ( ) {
        ProductEditionPopup.newPopup(null);
    }
    
    public void onEditProductRequest ( ) {
        ProductEditionPopup.newPopup(this.view.getSelectedProduct());
    }
    
    public void onRemoveProductRequest ( ) {
    }
}
