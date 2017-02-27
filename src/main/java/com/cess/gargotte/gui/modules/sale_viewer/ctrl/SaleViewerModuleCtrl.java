package com.cess.gargotte.gui.modules.sale_viewer.ctrl;

import com.cess.gargotte.core.model.IModel;
import com.cess.gargotte.gui.modules.sale_viewer.model.IObservableLog;
import com.cess.gargotte.gui.modules.sale_viewer.model.SaleViewerModuleModel;
import com.cess.gargotte.gui.modules.sale_viewer.view.SaleViewerModuleView;
import javafx.scene.control.Alert;
import javafx.stage.Modality;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guillaume on 25/02/2017.
 */
public class SaleViewerModuleCtrl {
    
    private SaleViewerModuleModel model;
    private SaleViewerModuleView view;
    
    public SaleViewerModuleCtrl (final SaleViewerModuleModel model) {
        this.model = model;
    }
    
    public void onError (final Throwable e) {
        Alert alert = new Alert(Alert.AlertType.ERROR, "Erreur : "+e.toString());
        alert.setTitle("Erreur");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();
    }
    
    public void setView (final SaleViewerModuleView view) {
        this.view = view;
    }
    
    public List<IObservableLog> getOrders (final int numberOfLogs){
        return model.getLastOrders(numberOfLogs);
    }
    
    public void onRefreshRequest () {
        this.view.updateData();
    }
}
