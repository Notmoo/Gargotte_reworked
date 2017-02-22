package com.cess.gargotte.gui.modules.stock.ctrl;

import com.cess.gargotte.core.model.IModel;
import com.cess.gargotte.gui.modules.stock.view.StockModuleView;

/**
 * Created by Guillaume on 22/02/2017.
 */
public class StockModuleCtrl {
    
    private IModel model;
    private StockModuleView view;
    
    public StockModuleCtrl (IModel model) {
        this.model = model;
    }
    
    public void setView (StockModuleView view) {
        this.view = view;
    }
}
