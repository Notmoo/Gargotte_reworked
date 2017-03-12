package com.cess.gargotte.gui.modules.order.view;

import com.cess.gargotte.core.model.products.IReadOnlyProduct;
import com.cess.gargotte.core.model.sales.PaymentMethod;
import com.cess.gargotte.core.model.sales.Sale;
import com.cess.gargotte.gui.modules.ModuleUtils;
import com.cess.gargotte.gui.modules.order.ctrl.OrderModuleCtrl;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * Created by Guillaume on 21/02/2017.
 */
public class OrderModuleView {
    
    private BorderPane mainPane;
    private OrderModuleCtrl ctrl;
    
    private TabPane productTabPane;
    private List<ProductListViewGUIComponent> lists;
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
    
        productTabPane = new TabPane();
        productTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        
        for(String category : this.ctrl.getCategories()){
            ProductListViewGUIComponent list = this.newProductListComponent(category);
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
        
        
        AnchorPane bottomPane = new AnchorPane();
        ToolBar bottomToolBar = new ToolBar();
        Button orderValidationButton = new Button("Valider la commande");
        orderValidationButton.setOnAction(event->ctrl.onOrderValidationRequest());
        bottomToolBar.getItems().add(orderValidationButton);
        
        bottomPane.getChildren().add(bottomToolBar);
        AnchorPane.setRightAnchor(bottomToolBar, 2d);
        
        mainPane.setBottom(bottomPane);
        
        updateData();
    }
    
    private void removeListFromGui(ProductListViewGUIComponent list){
        this.productTabPane.getTabs().removeIf(tab->tab.getText().equals(list.getCatName()));
        lists.remove(list);
    }
    
    private void addListToGui(ProductListViewGUIComponent list){
        this.lists.add(list);
        Tab tab = new Tab(list.getCatName());
        tab.setContent(list);
        this.productTabPane.getTabs().add(tab);
    }
    
    private void displayPrice (double price) {
        this.priceLabel.setText(String.format("Total : %.2f€", price));
    }
    
    private ProductListViewGUIComponent newProductListComponent (String catName ) {
        ProductListViewGUIComponent list = new ProductListViewGUIComponent(catName);
        
        list.setCellFactory((product)->new ListCell<IReadOnlyProduct>(){
                
                private Tooltip tooltip;
                
                @Override
                protected void updateItem (IReadOnlyProduct item, boolean empty) {
                    super.updateItem(item, empty);
                    if ( empty || item == null ) {
                        this.setText(null);
                        this.setGraphic(null);
                    } else {
                        this.setText(item.getName( ) + " (" + item.getAmountRemaining( ) + ")");
                    }
                    
                    this.updateTooltip(item, empty);
                }
    
                private void updateTooltip (IReadOnlyProduct item, boolean empty) {
                    if(empty && item==null){
                        this.tooltip = null;
                        super.setTooltip(null);
                    }else{
                        this.tooltip = new Tooltip(item.getRepresentation(0, true));
                        super.setTooltip(tooltip);
                    }
                }
            });
    
        list.setOnMouseClicked(event->ctrl.onAddProductToSaleRequest(list.getSelectionModel().getSelectedItem()));
        
        return list;
    }
    
    public void updateData ( ) {
        List<String> catCache = new ArrayList<>(), catFromModel = ctrl.getCategories();
        
        lists.forEach(list->catCache.add(list.getCatName()));
        
        for(String cat : catFromModel){
            if(!catCache.contains(cat)){
                catCache.add(cat);
                this.addListToGui(this.newProductListComponent(cat));
            }
        }
        for(String cat : catCache){
            if(!catFromModel.contains(cat)){
                for(ProductListViewGUIComponent list : new ArrayList<>(lists)){
                   if(list.getCatName().equals(cat)) {
                       removeListFromGui(list);
                   }
                }
            }
        }
        
        
        for(ProductListViewGUIComponent list : lists){
            Platform.runLater(()-> {
                list.getItems().clear();
                list.getItems().addAll(this.ctrl.getProducts(list.getCatName()));
            });
        }
        
        Platform.runLater(()-> saleListView.setItems(FXCollections.observableArrayList(ctrl.getOrderSales())));
        this.displayPrice(this.ctrl.getTotalPrice());
    }
    
    public Pane getMainPane(){
        return mainPane;
    }
    
    public void changeActionInfoLabelText(String text, boolean success){
        this.actionInfoLabel.setText(text);
        this.actionInfoLabel.setStyle("-fx-text-fill: "+(success? ModuleUtils.getSuccessLabelColor():ModuleUtils.getFailureLabelColor())+";");
    }
    
    public void resetOrder(){
        this.paymentMethodGroup.selectToggle(null);
    }
}
