package com.cess.gargotte.log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Guillaume on 21/02/2017.
 */
public class SaleLogSyntaxFactory {
    
    private static final Date DATE = new Date();
    private static final String FIELD_SEPARATOR = "\t";
    private final DateFormat dateFormat;
    
     public SaleLogSyntaxFactory(){
        dateFormat = new SimpleDateFormat("<E dd.MM.yy|HH:mm:ss>");
    }
    
    public ISaleLogSyntax newInlineSimpleSyntax(){
        return (sale, pm)->{
            StringBuffer sb = new StringBuffer();

            sb.append(dateFormat.format(DATE)).append(FIELD_SEPARATOR).append(sale.getProduct().getName()).append(FIELD_SEPARATOR).append(sale.getAmount()).append(FIELD_SEPARATOR).append(sale.getProduct().getPrice()*sale.getAmount()).append(FIELD_SEPARATOR).append(pm);

            return sb.toString();
        };
    }
}
