package com.cess.gargotte.gui.main_frame;

import com.cess.gargotte.gui.modules.IModule;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guillaume on 21/02/2017.
 */
public class MainFrame {
    
    private Stage stage;
    private MFCtrl ctrl;
    private MFView view;
    
    public MainFrame(Stage stage){
        this.stage = stage;
        this.ctrl = new MFCtrl();
        this.view = new MFView(ctrl);
        
        this.stage.setScene(new Scene(view.getMainPane(), 500, 500));
    }
    
    public void addModule(IModule module){
        this.view.addModule(module.getName(), module.getPane());
    }
    
    public void show(){
        this.stage.show();
    }
}
