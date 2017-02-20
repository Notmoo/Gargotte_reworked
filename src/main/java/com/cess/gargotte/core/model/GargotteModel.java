package com.cess.gargotte.core.model;

import com.cess.gargotte.core.model.listeners.IModelFirerer;
import com.cess.gargotte.core.model.listeners.ModelFirerer;
import com.cess.gargotte.core.model.products.IProduct;
import com.cess.gargotte.core.model.sales.Order;
import com.cess.gargotte.core.model.sales.ProductBuffer;
import com.cess.gargotte.core.model.sales.Sale;
import com.cess.gargotte.log.ILogger;
import com.cess.gargotte.log.SimpleLogger;
import com.cess.gargotte.reader.IIOHandler;
import com.cess.gargotte.reader.SerIOHandler;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Created by Guillaume on 19/02/2017.
 */
public class GargotteModel {

    private static final Path path = Paths.get("taverne.gardata");

    private IIOHandler ioHandler;
    private ILogger logger;
    private ProductBuffer productBuffer;
    private List<IProduct> products;
    private IModelFirerer firerer;


    public GargotteModel(){
        ioHandler = new SerIOHandler();
        logger = new SimpleLogger();
        products =  ioHandler.read();
        firerer = new ModelFirerer();
        productBuffer = new ProductBuffer();
    }

    public List<IProduct> getProducts() {
        return products;
    }

    public List<IProduct> getProducts(final String cat){
        final List<IProduct> reply = new ArrayList<IProduct>();
        this.products.stream().filter(new Predicate<IProduct>(){
            public boolean test(IProduct product){
                return product.getCat().equals(cat);
            }
        }).forEach(new Consumer<IProduct>() {
            public void accept(IProduct product) {
                reply.add(product);
            }
        });
        return reply;
    }

    public List<String> getCatList(){
        final List<String> reply = new ArrayList<String>();
        for(IProduct product : this.products){
            if(!reply.contains(product.getCat())){
                reply.add(product.getCat());
            }
        }
        return reply;
    }

    public boolean bufferSale(IProduct product){
        return productBuffer.addProduct(product);
    }

    public boolean unbufferSale(IProduct product){
       return productBuffer.removeProduct(product);
    }

    public boolean flushBufferedSales(){
       Order order = this.productBuffer.flush();
       this.productBuffer = new ProductBuffer();

       this.logger.log(order);
       //TODO Apply sur une copie, puis replacer la liste initiale (sécurité)
       this.apply(order);
       this.ioHandler.write(this.products);
       return true;
    }

    private void apply(Order order){
        for(Sale sale : order.getSales()){
            this.products.get(this.products.indexOf(sale.getProduct())).applySale(sale.getAmount());
        }
    }
}
