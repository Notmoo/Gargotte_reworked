package com.cess.gargotte.core.model.sales;

/**
 * Created by Guillaume on 22/02/2017.
 */
public enum PaymentMethod {
    ESPECE("Espèces"), CHEQUE("Chèque"), COMPTE_AE("Compte AE"), GRATUIT_INVITE("Gratuit (invité)"), GRATUIT_BENEVOLE("Gratuit (Bénévole)");
    
    private String text;
    
    private PaymentMethod(String text){
        this.text = text;
    }
    
    public String getText(){
        return text;
    }
}
