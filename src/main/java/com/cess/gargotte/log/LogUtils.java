package com.cess.gargotte.log;

/**
 * Created by Guillaume on 25/02/2017.
 */
public final class LogUtils {
    private LogUtils(){
    }
    
    public static ISaleLogSyntax getSaleLogSyntax(SaleLogSyntaxFactory factory){
        return factory.newInlineSimpleSyntax();
    }
}
