package com.cess.gargotte.log;

import com.cess.gargotte.core.model.sales.Sale;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Guillaume on 21/02/2017.
 */
public class SaleLogSyntaxFactory {
    
    private static final Date DATE = new Date();
    private final DateFormat dateFormat;
    
     public SaleLogSyntaxFactory(){
        dateFormat = new SimpleDateFormat("<E dd.MM.yy|HH:mm:ss>");
    }
    
    public ISaleLogSyntax newInlineSimpleSyntax(){
        return (sale)->{
            StringBuffer sb = new StringBuffer();

            sb.append(dateFormat.format(DATE)).append("\t").append(sale.getProduct().getName()).append("\t").append(sale.getAmount()).append("\t").append(sale.getProduct().getPrice()*sale.getAmount());

            return sb.toString();
        };
    }
}
