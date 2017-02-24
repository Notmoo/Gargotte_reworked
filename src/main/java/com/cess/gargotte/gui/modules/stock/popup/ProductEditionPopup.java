package com.cess.gargotte.gui.modules.stock.popup;

import com.cess.gargotte.core.model.IModel;
import com.cess.gargotte.core.model.products.ComposedProduct;
import com.cess.gargotte.core.model.products.IProduct;
import com.cess.gargotte.core.model.products.SimpleProduct;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
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

        ButtonType okButtonType = new ButtonType("Valider", ButtonBar.ButtonData.OK_DONE);
        ButtonType koButtonType = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, koButtonType);
    
        BorderPane mainPane = new BorderPane();
    
        GridPane centerPane = new GridPane();
        centerPane.setHgap(5);
        centerPane.setVgap(5);
        
        Label nameLabel = new Label("Nom");
        TextField nameTextField = new TextField();
        nameTextField.setPrefSize(CONTROL_PREF_WIDTH, CONTROL_PREF_HEIGHT);
    
        Label catLabel = new Label("Catégorie");
        ComboBox<String> catComboBox = new ComboBox<>();
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
    
        ListView<CheckableProduct> listView = new ListView<>();
        
        listView.setCellFactory(CheckBoxListCell.forListView(item -> item.checked, new StringConverter<CheckableProduct>( ) {
            @Override
            public String toString (CheckableProduct cp) {
                return cp.product.getName();
            }
    
            @Override
            public CheckableProduct fromString (String productName) {
                IProduct reply = null;
                for(IProduct currentProduct : model.getProducts()){
                    if(productName.equals(currentProduct.getName())){
                        reply = currentProduct;
                        break;
                    }
                }
                CheckableProduct cp = new CheckableProduct(reply);
                if(initialProduct.isComposedOf(reply))
                    cp.checked.setValue(true);
                return cp;
            }
        }));
        listView.getItems().forEach(cp->{
            cp.checked.addListener((obs, oldVal, newVal)->{
                if(ProductEditionPopup.hasComponents(listView)){
                    amountRemainingTextField.setDisable(true);
                }
            });
        });
        
        mainPane.setBottom(listView);
        
        if(initialProduct==null){
            dialog.setTitle("Ajout d'un produit");
            catComboBox.getSelectionModel().select("");
            List<CheckableProduct> cps = new ArrayList<>();
            model.getProducts().forEach(product->cps.add(new CheckableProduct(product)));
            Platform.runLater(()->listView.getItems().addAll(cps));
        }else{
            dialog.setTitle("Edition d'un produit");
    
            List<CheckableProduct> cps = new ArrayList<>();
            model.getProducts().stream().filter((product)->!product.isComposedOf(initialProduct)).forEach(product->{
                CheckableProduct cp = new CheckableProduct(product);
                if(initialProduct.isComposedOf(product))
                    cp.checked.setValue(true);
                cps.add(cp);
            });
            listView.getItems().addAll(cps);
            
            nameTextField.setText(initialProduct.getName());
            if(catComboBox.getItems().contains(initialProduct.getCat()))
                catComboBox.getSelectionModel().select(initialProduct.getCat());
            else
                catComboBox.setValue(initialProduct.getCat());
            priceTextField.setText(Double.toString(initialProduct.getPrice()));
            amountRemainingTextField.setText(Integer.toString(initialProduct.getAmountRemaining()));
            amountSoldTextField.setText(Integer.toString(initialProduct.getAmountSold()));
            
        }
        
        dialog.getDialogPane().setContent(mainPane);
        
        Node okButton = dialog.getDialogPane().lookupButton(okButtonType);
        okButton.addEventFilter(ActionEvent.ACTION, event->{
            if((nameTextField.getText().isEmpty()
                         || priceTextField.getText().isEmpty()
                         || catComboBox.getValue().isEmpty()
                         || amountRemainingTextField.getText().isEmpty()
                         || amountSoldTextField.getText().isEmpty())){
                event.consume();
                
                Alert alert = new Alert(Alert.AlertType.ERROR, "Vous devez remplir tous les champs.");
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.setTitle("Erreur");
                alert.showAndWait();
            }
        });
        
        dialog.setResultConverter(dialogButton -> {
            if(dialogButton.equals(okButtonType)){
                List<IProduct> components = new ArrayList<>();
                for(CheckableProduct cp : listView.getItems()) {
                    if ( cp.checked.getValue( ) ) {
                        components.add(cp.product);
                    }
                }
            
                String name = nameTextField.getText();
                String cat = catComboBox.getValue();
                double price = Double.parseDouble(priceTextField.getText());
                int amountRemaining = Integer.parseInt(amountRemainingTextField.getText());
                int amountSold = Integer.parseInt(amountSoldTextField.getText());
            
                if(components.size()==0){
                    return new SimpleProduct(name, cat, price, amountRemaining, amountSold);
                }else{
                    return new ComposedProduct(name, cat, price, amountSold, components);
                }
            }
            return null;
        });
    
        return dialog.showAndWait();
    }
    
    private static boolean hasComponents (ListView<CheckableProduct> listView) {
        boolean reply = false;
        for(CheckableProduct cp : listView.getItems()){
            reply = reply || cp.checked.getValue();
        }
        return reply;
    }
    
    private static class CheckableProduct{
        
        private IProduct product;
        private BooleanProperty checked;
        
        CheckableProduct(IProduct product){
            this.product = product;
            this.checked = new SimpleBooleanProperty(false);
        }
    }
}
