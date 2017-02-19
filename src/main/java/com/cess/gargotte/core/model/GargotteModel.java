package com.cess.gargotte.core.model;

import com.cess.gargotte.core.model.listeners.IModelFirerer;
import com.cess.gargotte.core.model.listeners.ModelFirerer;
import com.cess.gargotte.core.model.products.IProduct;
import com.cess.gargotte.log.ILogger;
import com.cess.gargotte.log.SimpleLogger;
import com.cess.gargotte.reader.IIOHandler;
import com.cess.gargotte.reader.SerIOHandler;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Guillaume on 19/02/2017.
 */
public class GargotteModel {

    private static final Path path = Paths.get("taverne.gardata");

    private IIOHandler ioHandler;
    private ILogger logger;

    private Map<String, List<IProduct>> products;
    private IModelFirerer firerer;


    public GargotteModel(){
        ioHandler = new SerIOHandler();
        logger = new SimpleLogger();
        products = new HashMap<String, List<IProduct>>();

        List<IProduct> products = ioHandler.read();
        for(IProduct product : products){
            if(!this.products.containsKey(product.getCat())){
                this.products.put(product.getCat(), new ArrayList<IProduct>());
            }
            this.products.get(product.getCat()).add(product);
        }

        firerer = new ModelFirerer();
    }

    public List<IProduct> getProducts(String cat){
        if(this.products.containsKey(cat)){
            return this.products.get(cat);
        }else{
            return new ArrayList<IProduct>();
        }
    }

    public List<String> getCatList(){
        return new ArrayList<String>(this.products.keySet());
    }

    public boolean bufferSale(IProduct product, int quantity){
        //TODO algo ventes
    }

    public boolean unbufferSale(IProduct, int quantity){
        //TODO algo ventes
    }

        public void flushBufferedSales(){
        //TODO algo ventes
    }
}
