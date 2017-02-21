package com.cess.gargotte.gui.modules.order.ctrl;

import com.cess.gargotte.core.model.GargotteModel;
import com.cess.gargotte.gui.modules.order.view.OrderModuleView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

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
}
