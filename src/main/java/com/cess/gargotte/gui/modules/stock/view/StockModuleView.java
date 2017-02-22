package com.cess.gargotte.gui.modules.stock.view;

import com.cess.gargotte.core.model.products.IProduct;
import com.cess.gargotte.gui.modules.stock.ctrl.StockModuleCtrl;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

/**
 * Created by Guillaume on 22/02/2017.
 */
public class StockModuleView {
    
    private StockModuleCtrl ctrl;
    private BorderPane mainPane;
    private TreeTableView<IProduct> stockTable;
    
    public StockModuleView (StockModuleCtrl ctrl) {
        this.ctrl = ctrl;
        
        this.mainPane = new BorderPane();
    
        stockTable = new TreeTableView<>();
        stockTable.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
        TreeItem<IProduct> root = new TreeItem<>();
        stockTable.setRoot(root);
        stockTable.setShowRoot(false);
        
        TreeTableColumn<IProduct, String> nameCol = new TreeTableColumn<>("Nom");
        TreeTableColumn<IProduct, String> catCol = new TreeTableColumn<>("Catégorie");
        TreeTableColumn<IProduct, String> priceCol = new TreeTableColumn<>("Prix");
        TreeTableColumn<IProduct, String> amountRemainingCol = new TreeTableColumn<>("En stock");
        TreeTableColumn<IProduct, String> amountSoldCol = new TreeTableColumn<>("Vendu");
        
        stockTable.getColumns().add(nameCol);
        stockTable.getColumns().add(catCol);
        stockTable.getColumns().add(priceCol);
        stockTable.getColumns().add(amountRemainingCol);
        stockTable.getColumns().add(amountSoldCol);
        
        mainPane.setCenter(stockTable);
    
        ToolBar rightToolBar = new ToolBar();
        rightToolBar.setOrientation(Orientation.VERTICAL);
        this.mainPane.setRight(rightToolBar);
    }
    
    public Pane getMainPane ( ) {
        return mainPane;
    }
}
