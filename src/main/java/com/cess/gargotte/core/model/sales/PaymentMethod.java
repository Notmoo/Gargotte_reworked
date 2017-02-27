package com.cess.gargotte.core.model.sales;

import java.util.EnumSet;

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
    
    public static PaymentMethod getPaymentMethod(String text){
        if(text == null)
            return null;
        
        EnumSet<PaymentMethod> pms = EnumSet.allOf(PaymentMethod.class);
        for(PaymentMethod pm : pms){
            if(text.equals(pm.getText())){
                return pm;
            }
        }
        
        return null;
    }
}
