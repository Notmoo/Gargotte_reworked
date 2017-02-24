package com.cess.gargotte.gui.modules;

/**
 * Created by Guillaume on 22/02/2017.
 */
public final class ModuleUtils {
    
    private static final String SUCCESS_FONT_COLOR = "#008b50", FAILURE_FONT_COLOR = "#bb271c";
    
    private ModuleUtils(){}
    
    public static String getSuccessLabelColor(){
        return SUCCESS_FONT_COLOR;
    }
    
    public static String getFailureLabelColor(){
        return FAILURE_FONT_COLOR;
    }
}
