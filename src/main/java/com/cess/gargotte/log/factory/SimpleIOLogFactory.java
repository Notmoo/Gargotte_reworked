package com.cess.gargotte.log.factory;

import com.cess.gargotte.core.model.products.IProduct;
import com.cess.gargotte.core.model.products.SimpleProduct;
import com.cess.gargotte.core.model.sales.Order;
import com.cess.gargotte.core.model.sales.PaymentMethod;
import com.cess.gargotte.core.model.sales.Sale;
import com.cess.gargotte.log.exceptions.UnknownProductException;
import com.cess.gargotte.log.factory.log.IOrderLogReader;
import com.cess.gargotte.log.factory.log.IOrderLogWriter;
import com.cess.gargotte.log.factory.log.ISaleLogReader;
import com.cess.gargotte.log.factory.log.ISaleLogWriter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guillaume on 21/02/2017.
 */
public class SimpleIOLogFactory implements IIOLogFactory{
    
    private final IIOFactoryParameters params;
    
    public SimpleIOLogFactory (IIOFactoryParameters params){
        this.params = params;
    }
    
    public ISaleLogWriter newSaleWriter (){
        return (sale)->String.format("%s%s%s%d%s%.2f€",
                                     params.getSalePrefix(),
                                     sale.getProduct().getName(),
                                     params.getFieldSeparator(),
                                     sale.getAmount(),
                                     params.getFieldSeparator(),
                                     sale.getProduct().getPrice()*sale.getAmount());
    }
    
    public ISaleLogReader newSaleReader (){
        return (str, products)->{
            if(str.length()<=params.getSalePrefix().length()){
                throw new IllegalArgumentException("Chaine <"+str+"> détectée comme invalide (longueur trop faible)");
            }
            
            String[] parts = str.split(params.getFieldSeparator());
            
            if(parts.length!=3)
                throw new IllegalArgumentException("Chaine <"+str+"> détectée comme invalide (Nombre de composants invalides)");
            
            IProduct product = null;
            for(IProduct currProduct : products){
                if(currProduct.getName().equals(parts[0]))
                    product = currProduct;
            }
            
            if(product == null){
                throw new UnknownProductException("Chaine <" + str + "> détectée comme invalide (Produit <" + parts[0] + " inconnu>)");
            }
            
            return new Sale(product, Integer.parseInt(parts[1]));
        };
    }
    
    public IOrderLogWriter newOrderWriter (){
        return (order, saleWriter)->{
            StringBuilder reply = new StringBuilder();
            
            reply.append(String.format("%s%s%s%s%.2f€%s",order.getTimeStamp(), params.getFieldSeparator(), order.getPaymentMethod().getText(), params.getFieldSeparator(), order.getTotalPrice(), params.getSaleSeparator()));
            
            for(Sale sale : order.getSales()){
                reply.append(saleWriter.write(sale)).append(params.getSaleSeparator());
            }
            
            return reply.append(params.getOrderSeparator()).append("\n").toString();
        };
    }
    
    public IOrderLogReader newOrderReader (){
        return (string, saleReader, products)->{
            String[] parts = string.split(params.getSalePrefix());
            if(parts.length<2){
                throw new IllegalArgumentException();
            }
    
            PaymentMethod pm = PaymentMethod.getPaymentMethod(parts[0].split(params.getFieldSeparator())[1]);
            if(pm == null){
                throw new IllegalArgumentException("Methode de paiement inconue : " +parts[0].split(params.getFieldSeparator())[1]);
            }
            
            List<Sale> sales = new ArrayList<>();
            for(int i = 1; i<parts.length; i++){
                try {
                    sales.add(saleReader.read(parts[i], products));
                } catch (UnknownProductException e) {
                    sales.add(new Sale(new SimpleProduct("PRODUIT INCONNU", "NONE", 0,0,0), 0));
                }
            }
            
            return new Order(sales, pm);
        };
    }
}
