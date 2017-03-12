package com.cess.gargotte.gui.modules.order.ctrl;

import com.cess.gargotte.core.model.IModel;
import com.cess.gargotte.core.model.products.IReadOnlyProduct;
import com.cess.gargotte.core.model.sales.PaymentMethod;
import com.cess.gargotte.core.model.sales.Sale;
import com.cess.gargotte.gui.modules.order.view.OrderModuleView;
import javafx.scene.control.Alert;
import javafx.scene.control.Toggle;

import java.util.List;

/**
 * Created by Guillaume on 21/02/2017.
 */
public class OrderModuleCtrl {
    
    private final IModel model;
    private OrderModuleView view;
    
    public OrderModuleCtrl (IModel model) {
        if(model == null){
            throw new NullPointerException();
        }
        this.model = model;
    }
    
    public void setView (OrderModuleView view) {
        this.view = view;
    }
    
    public List<String> getCategories ( ) {
        return model.getCatList();
    }
    
    public List<IReadOnlyProduct> getProducts (String category) {
        return this.model.getProductsFromCat(category);
    }
    
    public List<Sale> getOrderSales ( ) {
        return model.getCurrentOrder().getSales();
    }
    
    public double getTotalPrice ( ) {
        double price = 0;
        for(Sale sale : getOrderSales()){
            price+=(sale.getProduct().getPrice()*sale.getAmount());
        }
        
        return price;
    }
    
    public void onAddProductToSaleRequest (IReadOnlyProduct product) {
        if(product!=null){
            boolean success = this.model.bufferSale(product);
            if(success){
                this.view.changeActionInfoLabelText("Ajout effectué avec succès", true);
            }else{
                this.view.changeActionInfoLabelText("Echec : pas assez de stock", false);
            }
        }
    }
    
    public void onRemoveProductFromSaleRequest (Sale sale){
        if(sale!=null){
            boolean success = this.model.unbufferSale(sale.getProduct());
            if(success){
                this.view.changeActionInfoLabelText("Retrait effectué avec succès", true);
            }else{
                this.view.changeActionInfoLabelText("Echec", false);
            }
        }
    }
    
    public void onPaymentMethodChangeRequest (Toggle toggle) {
        if(toggle == null){
            this.model.setPaymentMethod(null);
        }else if(toggle.getUserData().getClass().equals(PaymentMethod.class)){
            this.model.setPaymentMethod((PaymentMethod) toggle.getUserData());
        }
    }
    
    public void onOrderValidationRequest(){
        if(this.model.getCurrentOrder().getSales().size()>0) {
            if ( this.model.getPaymentMethod( ) != null ) {
                boolean success = this.model.flushBufferedSales( );
                if ( success ) {
                    this.view.resetOrder();
                    this.view.changeActionInfoLabelText("Commande enregistrée avec succès", true);
                } else {
                    this.view.changeActionInfoLabelText("Echec de l'enregistrement de la commande", false);
                }
            } else {
                this.view.changeActionInfoLabelText("Echec de l'enregistrement de la commande : méthode de paiement non-sélectionnée", false);
            }
        }else{
            this.view.changeActionInfoLabelText("Echec de l'enregistrement de la commande : commande vide", false);
        }
    }
    
    public void onErrorEvent(Throwable e){
        new Alert(Alert.AlertType.ERROR, e.toString()).showAndWait();
    }
}
