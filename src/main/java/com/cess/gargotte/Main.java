package com.cess.gargotte;

import com.cess.gargotte.core.model.GargotteModel;
import com.cess.gargotte.core.model.IModel;
import com.cess.gargotte.gui.main_frame.MainFrame;
import com.cess.gargotte.gui.modules.order.OrderModule;
import com.cess.gargotte.gui.modules.sale_viewer.SaleViewerModule;
import com.cess.gargotte.gui.modules.stock.StockModule;
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
        IModel model = new GargotteModel();
        
        MainFrame mf = new MainFrame(primaryStage);
        mf.addModule(new OrderModule(model));
        mf.addModule(new StockModule(model));
        mf.addModule(new SaleViewerModule(model));
        
        mf.show();
    }
}
