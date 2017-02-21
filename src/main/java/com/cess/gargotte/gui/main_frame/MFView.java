package com.cess.gargotte.gui.main_frame;

import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

/**
 * Created by Guillaume on 21/02/2017.
 */
public class MFView {
    
    private BorderPane mainPane;
    private MFCtrl ctrl;
    private TabPane tabPane;
    
    public MFView (MFCtrl ctrl) {
        this.ctrl = ctrl;
        this.mainPane = new BorderPane();
        this.tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        mainPane.setCenter(tabPane);
    }
    
    public Parent getMainPane ( ) {
        return mainPane;
    }
    
    public void addModule(String name, Pane content){
        this.tabPane.getTabs().add(new Tab(name, content));
    }
}
