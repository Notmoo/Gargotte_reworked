package com.cess.gargotte;

import com.cess.gargotte.core.model.GargotteModel;
import com.cess.gargotte.gui.main_frame.MainFrame;
import com.cess.gargotte.gui.modules.order.OrderModule;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Created by Guillaume on 21/02/2017.
 */
public class Main extends Application{

    public static final void main(String[] args){
        launch(args);
    }
    
    @Override
    public void start (Stage primaryStage) throws Exception {
        GargotteModel model = new GargotteModel();
        
        MainFrame mf = new MainFrame(primaryStage);
        mf.addModule(new OrderModule(model));
        
        mf.show();
    }
}
