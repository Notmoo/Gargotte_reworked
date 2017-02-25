package com.cess.gargotte.gui.modules.stock.view;

import com.cess.gargotte.core.model.products.ComposedProduct;
import com.cess.gargotte.core.model.products.IProduct;
import com.cess.gargotte.gui.modules.ModuleUtils;
import com.cess.gargotte.gui.modules.stock.ctrl.StockModuleCtrl;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.ListChangeListener;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guillaume on 22/02/2017.
 */
public class StockModuleView {
    
    private static final double CONTROL_PREF_HEIGHT = 25, CONTROL_PREF_WIDTH = 125;
    
    private StockModuleCtrl ctrl;
    private BorderPane mainPane;
    private TreeTableView<IProduct> stockTable;
    
    private Label actionInfoLabel;
    private TextField passwordField;
    
    private ToolBar rightToolbar;
    private VBox lockedStateControlsVBox, unlockedStateControlsVBox;
    
    private BooleanProperty listEmpty;
    private BooleanProperty lockedState;
    
    public StockModuleView (StockModuleCtrl ctrl) {
        this.ctrl = ctrl;
        lockedState = new SimpleBooleanProperty(true);
        
        this.mainPane = new BorderPane();
    
        ToolBar topToolBar = new ToolBar();
        actionInfoLabel = new Label();
        topToolBar.getItems().add(actionInfoLabel);
        
        mainPane.setTop(topToolBar);
        
        stockTable = new TreeTableView<>();
        stockTable.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
        stockTable.setRowFactory(tv->{
            TreeTableRow<IProduct> row = new TreeTableRow<>();
            row.addEventFilter(MouseEvent.MOUSE_CLICKED, event->{
                if(!lockedState.getValue() && event.getClickCount()==2 && event.getButton() == MouseButton.PRIMARY){
                    this.ctrl.onEditProductRequest();
                }
            });
            return row;
        });
        
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
        
        listEmpty = new SimpleBooleanProperty(StockModuleView.this.stockTable.getRoot().getChildren().size()==0);
        this.stockTable.getRoot().getChildren().addListener(new ListChangeListener<TreeItem<IProduct>>() {
            @Override
            public void onChanged (final Change c) {
                StockModuleView.this.listEmpty.set(StockModuleView.this.stockTable.getRoot().getChildren().size()==0);
            }
        });
        
        mainPane.setCenter(stockTable);
        
        rightToolbar = new ToolBar();
        rightToolbar.setOrientation(Orientation.VERTICAL);
        
        passwordField = new TextField();
        passwordField.setPromptText("Mot de passe");
        passwordField.setPrefSize(CONTROL_PREF_WIDTH, CONTROL_PREF_HEIGHT);
        passwordField.setOnAction(event->this.ctrl.onUnlockAttempt(passwordField.getText()));
    
        lockedStateControlsVBox = new VBox();
        lockedStateControlsVBox.getChildren().add(passwordField);
        rightToolbar.getItems().add(lockedStateControlsVBox);
        
        unlockedStateControlsVBox = new VBox();
        
        Button addProductButton = new Button("Nouveau");
        addProductButton.setPrefSize(CONTROL_PREF_WIDTH, CONTROL_PREF_HEIGHT);
        addProductButton.setOnAction(event-> this.ctrl.onAddProductRequest());
        
        Button editProductButton = new Button("Modifier");
        editProductButton.setPrefSize(CONTROL_PREF_WIDTH, CONTROL_PREF_HEIGHT);
        editProductButton.setOnAction(event-> this.ctrl.onEditProductRequest());
        editProductButton.disableProperty().bind(this.stockTable.getSelectionModel().selectedItemProperty().isNull().or(listEmpty));
        
        Button removeProductButton = new Button("Supprimer");
        removeProductButton.setPrefSize(CONTROL_PREF_WIDTH, CONTROL_PREF_HEIGHT);
        removeProductButton.setOnAction(event-> this.ctrl.onRemoveProductRequest());
        removeProductButton.disableProperty().bind(this.stockTable.getSelectionModel().selectedItemProperty().isNull().or(listEmpty));
        
        unlockedStateControlsVBox.getChildren().addAll(addProductButton, editProductButton, removeProductButton);
        
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
        if(unlocked && this.lockedState.getValue()){
            Platform.runLater(()->{
                this.rightToolbar.getItems().remove(lockedStateControlsVBox);
                this.rightToolbar.getItems().add(unlockedStateControlsVBox);
            });
            this.lockedState.set(!unlocked);
        }else if(!unlocked && !this.lockedState.getValue()){
            Platform.runLater(()->{
                this.rightToolbar.getItems().remove(lockedStateControlsVBox);
                this.rightToolbar.getItems().add(unlockedStateControlsVBox);
            });
            this.lockedState.set(!unlocked);
        }else{
            throw new IllegalStateException();
        }
    }
    
    public IProduct getSelectedProduct ( ) {
        if(this.stockTable.getSelectionModel().getSelectedItem()!=null)
            return this.stockTable.getSelectionModel().getSelectedItem().getValue();
        else
            return null;
    }
}
