package com.cess.gargotte.gui.modules.stock.popup;

import com.cess.gargotte.core.model.products.IProduct;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.util.Optional;

/**
 * Created by Guillaume on 23/02/2017.
 */
public class ProductEditionPopup {
    
    private static final double CONTROL_PREF_HEIGHT = 25, CONTROL_PREF_WIDTH = 125;
    
    private ProductEditionPopup(){
    }
    
    public static Optional<IProduct> newPopup(IProduct initialProduct){
        
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
        
        Label nameLabel = new Label("Nom");
        TextField nameTextField = new TextField();
        nameTextField.setPrefSize(CONTROL_PREF_WIDTH, CONTROL_PREF_HEIGHT);
    
        Label catLabel = new Label("Catégorie");
        ComboBox catComboBox = new ComboBox();
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
        
        dialog.getDialogPane().setContent(mainPane);

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            //TODO Faire le result converter
            return null;
        });
    
        return dialog.showAndWait();
    }
}
