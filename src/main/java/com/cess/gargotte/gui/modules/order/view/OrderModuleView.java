package com.cess.gargotte.gui.modules.order.view;

import com.cess.gargotte.core.model.products.IProduct;
import com.cess.gargotte.core.model.sales.Sale;
import com.cess.gargotte.gui.modules.order.ctrl.OrderModuleCtrl;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * Created by Guillaume on 21/02/2017.
 */
public class OrderModuleView {
    
    private BorderPane mainPane;
    private OrderModuleCtrl ctrl;
    private TabPane productTabPane;
    private ListView<Sale> saleListView;
    private Label priceLabel;
    
    public OrderModuleView(OrderModuleCtrl ctrl){
        if(ctrl == null){
            throw new NullPointerException();
        }
        this.ctrl = ctrl;
        mainPane = new BorderPane();
    
        productTabPane = new TabPane();
        productTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        
        for(String category : this.ctrl.getCategories()){
            ListView<IProduct> list = this.newProductTableView();
            ListViewTab tab = new ListViewTab(category, list);
            productTabPane.getTabs().add(tab);
        }
        
        mainPane.setLeft(productTabPane);
        
        saleListView = new ListView<>();
        saleListView.setCellFactory((sale)-> new ListCell<Sale>(){
                @Override
                protected void updateItem (Sale item, boolean empty) {
                    if ( empty || item == null ) {
                        this.setText(null);
                        this.setGraphic(null);
                    } else {
                        this.setText(item.getProduct( ).getName( ) + " (" + item.getAmount( ) + "/" + item.getProduct( ).getAmountRemaining( )
                                             + ")");
                    }
                }
        });
        
        mainPane.setCenter(saleListView);
        
        ToolBar toolbar = new ToolBar();
        toolbar.setOrientation(Orientation.VERTICAL);
        priceLabel = new Label("Total : 0.00€");
        
        VBox paymentMethodVBox = new VBox();
        
        toolbar.getItems().addAll(priceLabel, new Separator(Orientation.VERTICAL), paymentMethodVBox);
        mainPane.setRight(toolbar);
        
        updateData();
    }
    
    private ListView<IProduct> newProductTableView ( ) {
        ListView<IProduct> list = new ListView<>();
    
        list.setCellFactory((product)-> new ListCell<IProduct>(){
                @Override
                protected void updateItem (IProduct item, boolean empty) {
                    if ( empty || item == null ) {
                        this.setText(null);
                        this.setGraphic(null);
                    } else {
                        this.setText(item.getName( ) + " (" + item.getAmountRemaining( ) + ")");
                    }
                }
        });
        
        return list;
    }
    
    public void updateData ( ) {
        for(Tab tab : productTabPane.getTabs()){
            if(tab.getClass().equals(ListViewTab.class)){
                Platform.runLater(()-> {
                    ( (ListViewTab) tab ).getListView( ).setItems(FXCollections.observableArrayList(this.ctrl.getProducts(tab.getText())));
                });
            }
        }
        
        Platform.runLater(()->{
            saleListView.setItems(FXCollections.observableArrayList(ctrl.getOrderSales()));
        });
    }
    
    public Pane getMainPane(){
        return mainPane;
    }
}
