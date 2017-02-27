package com.cess.gargotte.gui.modules.sale_viewer.view;

import com.cess.gargotte.gui.modules.sale_viewer.ctrl.SaleViewerModuleCtrl;
import com.cess.gargotte.gui.modules.sale_viewer.model.IObservableLog;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guillaume on 25/02/2017.
 */
public class SaleViewerModuleView {
    
    private static final int INITIAL_NB_LOG_DISPLAYED = 20;
    
    private SaleViewerModuleCtrl ctrl;
    private BorderPane mainPane;
    
    private TreeTableView<IObservableLog> logTable;
    private TextField nbLogDisplayedTextField;
    
    public SaleViewerModuleView (final SaleViewerModuleCtrl ctrl) {
        this.ctrl = ctrl;
        this.mainPane = new BorderPane();
    
        ToolBar topToolBar = new ToolBar();
        Label nbLogDisplayedLabel = new Label("Nombre de lignes à afficher : ");
        nbLogDisplayedTextField = new TextField(Integer.toString(INITIAL_NB_LOG_DISPLAYED));
        nbLogDisplayedTextField.setPrefWidth(50);
        
        
        Button refreshButton = new Button("Mettre à jour");
        refreshButton.setOnAction(event->this.ctrl.onRefreshRequest());
        
        topToolBar.getItems().addAll(nbLogDisplayedLabel, nbLogDisplayedTextField, new Separator(), refreshButton);
        this.mainPane.setTop(topToolBar);
        
        this.logTable = new TreeTableView<>();
        TreeItem<IObservableLog> root = new TreeItem<>();
        this.logTable.setRoot(root);
        this.logTable.setShowRoot(false);
        this.logTable.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
    
        TreeTableColumn<IObservableLog, String> timeStampCol = new TreeTableColumn<>("Horodateur");
        timeStampCol.setCellValueFactory(cell->cell.getValue().getValue().timeStamp());
        this.logTable.getColumns().add(timeStampCol);
    
        TreeTableColumn<IObservableLog, String> nameCol = new TreeTableColumn<>("Produit");
        nameCol.setCellValueFactory(cell->cell.getValue().getValue().name());
        this.logTable.getColumns().add(nameCol);
    
        TreeTableColumn<IObservableLog, String> amountCol = new TreeTableColumn<>("Nombre");
        amountCol.setCellValueFactory(cell->cell.getValue().getValue().amount());
        this.logTable.getColumns().add(amountCol);
    
        TreeTableColumn<IObservableLog, String> priceCol = new TreeTableColumn<>("Prix total");
        priceCol.setCellValueFactory(cell->cell.getValue().getValue().price());
        this.logTable.getColumns().add(priceCol);
    
        TreeTableColumn<IObservableLog, String> paymentMethodCol = new TreeTableColumn<>("Règlement");
        paymentMethodCol.setCellValueFactory(cell->cell.getValue().getValue().paymentMethod());
        this.logTable.getColumns().add(paymentMethodCol);
        
        this.mainPane.setCenter(logTable);
    }
    
    private List<TreeItem<IObservableLog>> generateTree (final List<IObservableLog> orders) {
        List<TreeItem<IObservableLog>> items = new ArrayList<>();
        if(orders!=null) {
            for (IObservableLog order : orders) {
                TreeItem<IObservableLog> item = new TreeItem<>(order);
                if (order.content() != null) {
                    item.getChildren().addAll(this.generateTree(order.content()));
                }
                items.add(item);
            }
        }
        
        return items;
    }
    
    public void updateData () {
        int nbLogDisplayed;
        try{
            nbLogDisplayed = Integer.parseInt(this.nbLogDisplayedTextField.getText());
    
            List<TreeItem<IObservableLog>> newContent = this.generateTree(this.ctrl.getOrders(nbLogDisplayed));
    
            Platform.runLater(()->{
                logTable.getRoot().getChildren().clear();
                logTable.getRoot().getChildren().addAll(newContent);
            });
        }catch(NumberFormatException e){
            this.ctrl.onError(e);
        }
    }
    
    public Pane getMainPane () {
        return mainPane;
    }
}
