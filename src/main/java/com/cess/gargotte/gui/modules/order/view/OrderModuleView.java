package com.cess.gargotte.gui.modules.order.view;

import com.cess.gargotte.core.model.products.IProduct;
import com.cess.gargotte.core.model.sales.PaymentMethod;
import com.cess.gargotte.core.model.sales.Sale;
import com.cess.gargotte.gui.modules.order.ctrl.OrderModuleCtrl;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * Created by Guillaume on 21/02/2017.
 */
public class OrderModuleView {
    
    private static final String SUCCESS_FONT_COLOR = "#008b50", FAILURE_FONT_COLOR = "#bb271c";
    
    private BorderPane mainPane;
    private OrderModuleCtrl ctrl;
    private List<ProductListView> lists;
    private ListView<Sale> saleListView;
    
    private Label priceLabel;
    private Label actionInfoLabel;
    private ToggleGroup paymentMethodGroup;
    
    public OrderModuleView(OrderModuleCtrl ctrl){
        if(ctrl == null){
            throw new NullPointerException();
        }
        this.ctrl = ctrl;
        this.lists = new ArrayList<>();
        mainPane = new BorderPane();
    
        TabPane productTabPane = new TabPane();
        productTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        
        for(String category : this.ctrl.getCategories()){
            ProductListView list = this.newProductListView(category);
            list.setOnMouseClicked(event->ctrl.onAddProductToSaleRequest(list.getSelectionModel().getSelectedItem()));
            Tab tab = new Tab(category, list);
            this.lists.add(list);
            productTabPane.getTabs().add(tab);
        }
        
        mainPane.setLeft(productTabPane);
        
        saleListView = new ListView<>();
        saleListView.setCellFactory((sale)-> new ListCell<Sale>(){
                @Override
                protected void updateItem (Sale item, boolean empty) {
                    super.updateItem(item, empty);
                    if ( empty || item == null ) {
                        this.setText(null);
                        this.setGraphic(null);
                    } else {
                        this.setText(String.format("%s x%d- %.2f€",item.getProduct( ).getName( ), item.getAmount( ),item.getAmount()*item.getProduct().getPrice()));
                    }
                }
        });
        saleListView.setOnMouseClicked(event->ctrl.onRemoveProductFromSaleRequest(saleListView.getSelectionModel().getSelectedItem()));
        
        mainPane.setCenter(saleListView);
    
        ToolBar topToolbar = new ToolBar();
        actionInfoLabel = new Label();
        topToolbar.getItems().add(actionInfoLabel);
        
        mainPane.setTop(topToolbar);
        
        ToolBar rightToolbar = new ToolBar();
        rightToolbar.setOrientation(Orientation.VERTICAL);
        priceLabel = new Label();
        
        VBox paymentMethodVBox = new VBox();
        EnumSet<PaymentMethod> paymentMethods = EnumSet.allOf(PaymentMethod.class);
        paymentMethodGroup = new ToggleGroup();
        paymentMethodGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle)-> this.ctrl.onPaymentMethodChangeRequest(newToggle));
        
        for(PaymentMethod pm : paymentMethods){
            RadioButton toggle = new RadioButton(pm.getText());
            toggle.setUserData(pm);
            paymentMethodGroup.getToggles().add(toggle);
            paymentMethodVBox.getChildren().add(toggle);
        }
        
        rightToolbar.getItems().addAll(priceLabel, new Separator(Orientation.VERTICAL), paymentMethodVBox);
        mainPane.setRight(rightToolbar);
        
        updateData();
    }
    
    private void displayPrice (double price) {
        this.priceLabel.setText(String.format("Total : %.2f€", price));
    }
    
    private ProductListView newProductListView (String catName ) {
        ProductListView list = new ProductListView(catName);
        
        list.setCellFactory((product)-> new ListCell<IProduct>(){
                @Override
                protected void updateItem (IProduct item, boolean empty) {
                    super.updateItem(item, empty);
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
        for(ProductListView list : lists){
            Platform.runLater(()-> list.setItems(FXCollections.observableArrayList(this.ctrl.getProducts(list.getCatName()))));
        }
        
        Platform.runLater(()-> saleListView.setItems(FXCollections.observableArrayList(ctrl.getOrderSales())));
        this.displayPrice(this.ctrl.getTotalPrice());
    }
    
    public Pane getMainPane(){
        return mainPane;
    }
    
    public void changeActionInfoLabelText(String text, boolean success){
        this.actionInfoLabel.setText(text);
        this.actionInfoLabel.setStyle("-fx-text-fill: "+(success?SUCCESS_FONT_COLOR:FAILURE_FONT_COLOR)+";");
    }
}
