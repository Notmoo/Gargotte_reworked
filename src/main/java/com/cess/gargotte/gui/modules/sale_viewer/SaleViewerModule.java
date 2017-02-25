package com.cess.gargotte.gui.modules.sale_viewer;

import com.cess.gargotte.core.model.IModel;
import com.cess.gargotte.core.model.listeners.IModelListener;
import com.cess.gargotte.core.model.listeners.ModelListenerAdapter;
import com.cess.gargotte.gui.modules.IModule;
import com.cess.gargotte.gui.modules.sale_viewer.ctrl.SaleViewerModuleCtrl;
import com.cess.gargotte.gui.modules.sale_viewer.view.SaleViewerModuleView;
import javafx.scene.layout.Pane;

/**
 * Created by Guillaume on 25/02/2017.
 */
public class SaleViewerModule implements IModule{
    
    private String name;
    
    private SaleViewerModuleCtrl ctrl;
    private SaleViewerModuleView view;
    
    public SaleViewerModule(IModel model){
        this.name = "Historique des ventes";
        this.ctrl = new SaleViewerModuleCtrl(model);
        this.view = new SaleViewerModuleView(ctrl);
        
        this.ctrl.setView(view);
    }
    
    @Override
    public String getName () {
        return this.name;
    }
    
    @Override
    public Pane getPane () {
        return this.view.getMainPane();
    }
}
