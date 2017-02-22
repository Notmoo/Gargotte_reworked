package com.cess.gargotte.gui.modules.stock.view;

import com.cess.gargotte.core.model.products.ComposedProduct;
import com.cess.gargotte.core.model.products.IProduct;
import com.cess.gargotte.gui.modules.ModuleUtils;
import com.cess.gargotte.gui.modules.stock.ctrl.StockModuleCtrl;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guillaume on 22/02/2017.
 */
public class StockModuleView {
    
    private StockModuleCtrl ctrl;
    private BorderPane mainPane;
    private TreeTableView<IProduct> stockTable;
    
    private Label actionInfoLabel;
    private TextField passwordField;
    
    private ToolBar rightToolbar;
    private VBox lockedStateControlsVBox, unlockedStateControlsVBox;
    
    public StockModuleView (StockModuleCtrl ctrl) {
        this.ctrl = ctrl;
        
        this.mainPane = new BorderPane();
    
        ToolBar topToolBar = new ToolBar();
        actionInfoLabel = new Label();
        topToolBar.getItems().add(actionInfoLabel);
        
        mainPane.setTop(topToolBar);
        
        stockTable = new TreeTableView<>();
        stockTable.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
        TreeItem<IProduct> root = new TreeItem<>();
        stockTable.setRoot(root);
        stockTable.setShowRoot(false);
        stockTable.setEditable(false);
        
        TreeTableColumn<IProduct, String> nameCol = new TreeTableColumn<>("Nom");
        nameCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getValue().getName()));
        
        TreeTableColumn<IProduct, String> catCol = new TreeTableColumn<>("Catégorie");
        catCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getValue().getCat()));
        
        TreeTableColumn<IProduct, String> priceCol = new TreeTableColumn<>("Prix");
        priceCol.setCellValueFactory(cell -> new SimpleStringProperty(String.format("%.2f€",cell.getValue().getValue().getPrice())));
        
        TreeTableColumn<IProduct, String> amountRemainingCol = new TreeTableColumn<>("En stock");
        amountRemainingCol.setCellValueFactory(cell -> new SimpleStringProperty(Integer.toString(cell.getValue().getValue().getAmountRemaining())));
        
        TreeTableColumn<IProduct, String> amountSoldCol = new TreeTableColumn<>("Vendu");
        amountSoldCol.setCellValueFactory(cell -> new SimpleStringProperty(Integer.toString(cell.getValue().getValue().getAmountSold())));
        
        stockTable.getColumns().add(nameCol);
        stockTable.getColumns().add(catCol);
        stockTable.getColumns().add(priceCol);
        stockTable.getColumns().add(amountRemainingCol);
        stockTable.getColumns().add(amountSoldCol);
        
        mainPane.setCenter(stockTable);
        
        rightToolbar = new ToolBar();
        rightToolbar.setOrientation(Orientation.VERTICAL);
        
        lockedStateControlsVBox = new VBox();
        unlockedStateControlsVBox = new VBox();
        
        passwordField = new TextField();
        passwordField.setPromptText("Mot de passe");
        passwordField.setOnAction(event->{
            this.ctrl.onUnlockAttempt(passwordField.getText());
        });
        passwordField.requestFocus();
        
        lockedStateControlsVBox.getChildren().add(passwordField);
        
        rightToolbar.getItems().add(lockedStateControlsVBox);
        
        this.mainPane.setRight(rightToolbar);
    }
    
    public Pane getMainPane ( ) {
        return mainPane;
    }
    
    public void updateData(){
        List<TreeItem<IProduct>> items = this.generateTreeItems(this.ctrl.getProducts());
        Platform.runLater(()->{
            this.stockTable.getRoot().getChildren().clear();
            this.stockTable.getRoot().getChildren().addAll(items);
        });
    }
    
    private List<TreeItem<IProduct>> generateTreeItems (List<IProduct> products) {
        List<TreeItem<IProduct>> reply = new ArrayList<>();
        
        for(IProduct product : products){
            TreeItem<IProduct> item = new TreeItem<>(product);
            if(product.getClass().equals(ComposedProduct.class)){
                item.getChildren().addAll(generateTreeItems(((ComposedProduct)product).getComponents()));
            }
            reply.add(item);
        }
        
        return reply;
    }
    
    public void changeActionInfoLabelText(String text, boolean success){
        this.actionInfoLabel.setText(text);
        this.actionInfoLabel.setStyle("-fx-text-fill: "+(success? ModuleUtils.getSuccessLabelColor():ModuleUtils.getFailureLabelColor())+";");
    }
    
    public void setViewUnlocked (boolean unlocked){
        if(unlocked && this.rightToolbar.getItems().contains(lockedStateControlsVBox)){
            this.rightToolbar.getItems().remove(lockedStateControlsVBox);
            this.rightToolbar.getItems().add(unlockedStateControlsVBox);
        }else if(!unlocked && this.rightToolbar.getItems().contains(unlockedStateControlsVBox)){
            this.rightToolbar.getItems().remove(lockedStateControlsVBox);
            this.rightToolbar.getItems().add(unlockedStateControlsVBox);
        }
    }
}
