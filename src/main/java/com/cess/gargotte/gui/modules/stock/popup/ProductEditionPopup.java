package com.cess.gargotte.gui.modules.stock.popup;

import com.cess.gargotte.core.model.IModel;
import com.cess.gargotte.core.model.products.IProduct;
import com.cess.gargotte.core.model.products.SimpleProduct;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Guillaume on 23/02/2017.
 */
public class ProductEditionPopup {
    
    private static final double CONTROL_PREF_HEIGHT = 25, CONTROL_PREF_WIDTH = 125;
    
    private ProductEditionPopup(){
    }
    
    public static Optional<IProduct> newPopup(final IProduct initialProduct, final IModel model){
        
        Dialog<IProduct> dialog = new Dialog<>();
        if(initialProduct==null){
            dialog.setTitle("Ajout d'un produit");
        }else{
            dialog.setTitle("Edition d'un produit");
        }

        ButtonType okButtonType = new ButtonType("Valider", ButtonBar.ButtonData.OK_DONE);
        ButtonType koButtonType = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, koButtonType);
        
        Node loginButton = dialog.getDialogPane().lookupButton(okButtonType);
        loginButton.setDisable(true);
    
        BorderPane mainPane = new BorderPane();
    
        GridPane centerPane = new GridPane();
        centerPane.setHgap(5);
        centerPane.setVgap(5);
        
        Label nameLabel = new Label("Nom");
        TextField nameTextField = new TextField();
        nameTextField.setPrefSize(CONTROL_PREF_WIDTH, CONTROL_PREF_HEIGHT);
    
        Label catLabel = new Label("Catégorie");
        ComboBox catComboBox = new ComboBox();
        catComboBox.setEditable(true);
        catComboBox.getEditor().textProperty().addListener((obs, oldText, newText) -> catComboBox.setValue(newText));
        catComboBox.setItems(FXCollections.observableArrayList(model.getCatList()));
        catComboBox.setPrefSize(CONTROL_PREF_WIDTH, CONTROL_PREF_HEIGHT);
        
        Label priceLabel = new Label("Prix");
        TextField priceTextField = new TextField();
        priceTextField.setPrefSize(CONTROL_PREF_WIDTH, CONTROL_PREF_HEIGHT);
        
        Label amountRemainingLabel = new Label("Nombre en stock");
        TextField amountRemainingTextField = new TextField();
        amountRemainingTextField.setPrefSize(CONTROL_PREF_WIDTH, CONTROL_PREF_HEIGHT);
        
        Label amountSoldLabel = new Label("Nombre Vendu");
        TextField amountSoldTextField = new TextField();
        amountSoldTextField.setPrefSize(CONTROL_PREF_WIDTH, CONTROL_PREF_HEIGHT);
        
        centerPane.add(nameLabel, 0,0);
        centerPane.add(nameTextField, 1,0);
        centerPane.add(catLabel, 0,1);
        centerPane.add(catComboBox, 1,1);
        centerPane.add(priceLabel, 0,2);
        centerPane.add(priceTextField, 1,2);
        centerPane.add(amountRemainingLabel, 0,3);
        centerPane.add(amountRemainingTextField, 1,3);
        centerPane.add(amountSoldLabel, 0,4);
        centerPane.add(amountSoldTextField, 1,4);
        
        mainPane.setCenter(centerPane);
    
        ListView<IProduct> listView = new ListView<>();
        
        listView.setCellFactory(CheckBoxListCell.forListView(item -> new SimpleBooleanProperty( ), new StringConverter<IProduct>( ) {
            @Override
            public String toString (IProduct product) {
                return product.getName();
            }
    
            @Override
            public IProduct fromString (String productName) {
                IProduct reply = null;
                for(IProduct currentProduct : model.getProducts()){
                    if(productName.equals(currentProduct)){
                        reply = currentProduct;
                        break;
                    }
                }
                return reply;
            }
        }));
        
        if(initialProduct==null)
            Platform.runLater(()->listView.getItems().addAll(model.getProducts()));
        else{
            List<IProduct> products = new ArrayList<>();
            model.getProducts().stream().filter((product)->!initialProduct.isComposedOf(product) && !product.isComposedOf(initialProduct)).forEach(product->products.add(product));
        }
        
        mainPane.setBottom(listView);
        
        dialog.getDialogPane().setContent(mainPane);

        dialog.setResultConverter(dialogButton -> {
            //TODO Faire le result converter
            return null;
        });
    
        return dialog.showAndWait();
    }
}
