package com.cess.gargotte.log;

import java.text.Format;

/**
 * Created by Guillaume on 21/02/2017.
 */
public class SaleLogSyntaxFactory {
    public ISaleLogSyntax newInlineSimpleSyntax(){
        return (sale)->{
            StringBuffer sb = new StringBuffer();

            sb.append(sale.getProduct().getName()).append("\t").append(sale.getAmount()).append("\t").append(sale.getProduct().getPrice()*sale.getAmount());

            return sb.toString();
        };
    }
}
