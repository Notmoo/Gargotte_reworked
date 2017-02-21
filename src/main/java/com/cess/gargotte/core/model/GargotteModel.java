package com.cess.gargotte.core.model;

import com.cess.gargotte.core.model.listeners.IModelFirerer;
import com.cess.gargotte.core.model.listeners.IModelListener;
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

    private static final Path PATH = Paths.get("produits.gargotte");

    private IIOHandler ioHandler;
    private ILogger logger;
    private ProductBuffer productBuffer;
    private List<IProduct> products;

    private IModelFirerer dataEventFirerer;
    private IModelFirerer modelStateFirerer;


    public GargotteModel(){
        ioHandler = new SerIOHandler(PATH);
        logger = new SimpleLogger();
        productBuffer = new ProductBuffer();

        initProductList();

        dataEventFirerer = new ModelFirerer();
        modelStateFirerer = new ModelFirerer();
    }

    private void initProductList() {
        products = ioHandler.read();
    }

    public List<IProduct> getProductsFromCat() {
        return products;
    }

    public List<IProduct> getProductsFromCat(final String cat){
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

    public Order getCurrentOrder(){
        return this.productBuffer.makeOrder();
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
       Order order = this.productBuffer.makeOrder();
       this.productBuffer = new ProductBuffer();

       this.logger.log(order);
       this.apply(order);
       this.ioHandler.write(this.products);
       return true;
    }

    private void apply(Order order){
        try {
            for (Sale sale : order.getSales()) {
                this.products.get(this.products.indexOf(sale.getProduct())).applySale(sale.getAmount());
            }
        }catch(IllegalStateException e){
            //En cas d'erreur, on regénère la liste des produits à partir du fichier de sauvegarde.
            this.initProductList();
            this.modelStateFirerer.fireErrorEvent(e);
        }finally{
            this.dataEventFirerer.fireDataChangedEvent();
        }
    }

    public void addDataListener(IModelListener l){
        this.dataEventFirerer.addListener(l);
    }

    public void removeDataListener(IModelListener l){
        this.dataEventFirerer.removeListener(l);
    }

    public void addStateListener(IModelListener l){
        this.modelStateFirerer.addListener(l);
    }

    public void removeStateListener(IModelListener l){
        this.modelStateFirerer.removeListener(l);
    }
}
