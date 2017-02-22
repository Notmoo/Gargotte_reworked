package com.cess.gargotte.gui.modules.order;

import com.cess.gargotte.core.model.GargotteModel;
import com.cess.gargotte.core.model.IModel;
import com.cess.gargotte.core.model.listeners.IModelListener;
import com.cess.gargotte.gui.modules.IModule;
import com.cess.gargotte.gui.modules.order.ctrl.OrderModuleCtrl;
import com.cess.gargotte.gui.modules.order.view.OrderModuleView;
import javafx.scene.layout.Pane;

/**
 * Created by Guillaume on 21/02/2017.
 */
public class OrderModule implements IModule {
    
    private final String name;
    private final OrderModuleCtrl ctrl;
    private final OrderModuleView view;
    
    public OrderModule(IModel model){
        this.name = "Caisse";
        this.ctrl = new OrderModuleCtrl(model);
        this.view = new OrderModuleView(ctrl);
        
        this.ctrl.setView(view);
        
        model.addDataListener(new IModelListener( ) {
            @Override
            public void onDataChangedEvent ( ) {
                OrderModule.this.view.updateData();
            }
    
            @Override
            public void onErrorEvent (Throwable e) {
            }
        });
        model.addStateListener(new IModelListener( ) {
            @Override
            public void onDataChangedEvent ( ) {
            }
    
            @Override
            public void onErrorEvent (Throwable e) {
                OrderModule.this.ctrl.onErrorEvent(e);
            }
        });
    }
    
    @Override
    public String getName ( ) {
        return name;
    }
    
    @Override
    public Pane getPane ( ) {
        return view.getMainPane();
    }
}
