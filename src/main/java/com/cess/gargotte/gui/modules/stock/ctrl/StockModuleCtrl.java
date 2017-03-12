package com.cess.gargotte.gui.modules.stock.ctrl;

import com.cess.gargotte.core.model.IModel;
import com.cess.gargotte.core.model.products.IReadOnlyProduct;
import com.cess.gargotte.gui.modules.stock.popup.ProductEditionPopup;
import com.cess.gargotte.gui.modules.stock.view.StockModuleView;

import java.util.List;
import java.util.Optional;

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
    
    public List<IReadOnlyProduct> getProducts ( ) {
        return model.getProducts();
    }
    
    public void onUnlockAttempt (String text) {
        if(text.equals(PASSWORD)){
            this.view.setViewUnlocked(true);
        }
    }
    
    public void onAddProductRequest ( ) {
        Optional<IReadOnlyProduct> result = ProductEditionPopup.newPopup(null, model);
        result.ifPresent((product)->{
            model.addProduct(product);
            this.view.changeActionInfoLabelText("Produit ajouté", true);
        });
    }
    
    public void onEditProductRequest ( ) {
        if(this.view.getSelectedProduct()!=null) {
            Optional<IReadOnlyProduct> result = ProductEditionPopup.newPopup(this.view.getSelectedProduct(), model);
            result.ifPresent(product -> {
                model.replaceProduct(this.view.getSelectedProduct(), product);
                this.view.changeActionInfoLabelText("Produit modifié", true);
            });
        }
    }
    
    public void onRemoveProductRequest ( ) {
        if(this.view.getSelectedProduct()!=null) {
            model.removeProduct(this.view.getSelectedProduct());
            this.view.changeActionInfoLabelText("Produit retiré", true);
        }
    }
}
