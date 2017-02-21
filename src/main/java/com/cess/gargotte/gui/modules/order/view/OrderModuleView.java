package com.cess.gargotte.gui.modules.order.view;

import com.cess.gargotte.gui.modules.order.ctrl.OrderModuleCtrl;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

/**
 * Created by Guillaume on 21/02/2017.
 */
public class OrderModuleView {
    
    private BorderPane mainPane;
    private OrderModuleCtrl ctrl;
    
    public OrderModuleView(OrderModuleCtrl ctrl){
        if(ctrl == null){
            throw new NullPointerException();
        }
        this.ctrl = ctrl;
        mainPane = new BorderPane();
    }
    
    public void updateData ( ) {
        
    }
    
    public Pane getMainPane(){
        return mainPane;
    }
}
