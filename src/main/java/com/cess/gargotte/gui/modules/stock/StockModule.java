package com.cess.gargotte.gui.modules.stock;

import com.cess.gargotte.core.model.IModel;
import com.cess.gargotte.core.model.listeners.IModelListener;
import com.cess.gargotte.core.model.listeners.ModelListenerAdapter;
import com.cess.gargotte.gui.modules.IModule;
import com.cess.gargotte.gui.modules.stock.ctrl.StockModuleCtrl;
import com.cess.gargotte.gui.modules.stock.view.StockModuleView;
import javafx.scene.layout.Pane;

/**
 * Created by Guillaume on 22/02/2017.
 */
public class StockModule implements IModule {
    
    
    private final String name;
    private final StockModuleCtrl ctrl;
    private final StockModuleView view;
    
    public StockModule(IModel model){
        this.name = "Stock";
        this.ctrl = new StockModuleCtrl(model);
        this.view = new StockModuleView(ctrl);
        
        this.ctrl.setView(view);
        this.view.updateData();
        
        model.addDataListener(new ModelListenerAdapter( ) {
            @Override
            public void onDataChangedEvent ( ) {
                StockModule.this.view.updateData();
            }
        });
    }
    
    @Override
    public String getName () {
        return name;
    }
    
    @Override
    public Pane getPane () {
        return this.view.getMainPane();
    }
}
