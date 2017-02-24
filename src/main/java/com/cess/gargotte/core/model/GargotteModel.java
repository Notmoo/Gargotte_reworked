package com.cess.gargotte.core.model;

import com.cess.gargotte.core.model.listeners.IModelFirerer;
import com.cess.gargotte.core.model.listeners.IModelListener;
import com.cess.gargotte.core.model.listeners.ModelFirerer;
import com.cess.gargotte.core.model.products.IProduct;
import com.cess.gargotte.core.model.sales.Order;
import com.cess.gargotte.core.model.sales.PaymentMethod;
import com.cess.gargotte.core.model.sales.ProductBuffer;
import com.cess.gargotte.core.model.sales.Sale;
import com.cess.gargotte.log.IOrderLogger;
import com.cess.gargotte.log.SaleLogSyntaxFactory;
import com.cess.gargotte.log.SimpleOrderLogger;
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
public class GargotteModel implements IModel{

    private static final Path PRODUCT_FILE_PATH = Paths.get("produits.gargotte"), SALES_LOG_FILE_PATH = Paths.get("ventes.log");

    private IIOHandler ioHandler;
    private IOrderLogger logger;
    
    private List<IProduct> products;
    
    private PaymentMethod paymentMethod;
    private ProductBuffer productBuffer;

    private IModelFirerer dataEventFirerer;
    private IModelFirerer modelStateFirerer;


    public GargotteModel(){
        ioHandler = new SerIOHandler(PRODUCT_FILE_PATH);
        logger = new SimpleOrderLogger(SALES_LOG_FILE_PATH, new SaleLogSyntaxFactory());
        productBuffer = new ProductBuffer();

        initProductList();

        dataEventFirerer = new ModelFirerer();
        modelStateFirerer = new ModelFirerer();
    }

    private void initProductList() {
        products = ioHandler.read();
    }

    public List<IProduct> getProducts() {
        return products;
    }

    public List<IProduct> getProductsFromCat(final String cat){
        final List<IProduct> reply = new ArrayList<>( );
        if(cat!=null ) {
            this.products.stream( ).filter((product) -> product.getCat( ).equals(cat)).forEach((product) -> reply.add(product));
        }
        return reply;
    }

    public Order getCurrentOrder(){
        return this.productBuffer.makeOrder(paymentMethod);
    }

    public List<String> getCatList(){
        final List<String> reply = new ArrayList<>();
        for(IProduct product : this.products){
            if(!reply.contains(product.getCat())){
                reply.add(product.getCat());
            }
        }
        return reply;
    }

    public boolean bufferSale(IProduct product){
        boolean reply = productBuffer.addProduct(product);
        if(reply){
            this.dataEventFirerer.fireDataChangedEvent();
        }
        return reply;
    }

    public boolean unbufferSale(IProduct product){
        boolean reply = productBuffer.removeProduct(product);
        if(reply){
            this.dataEventFirerer.fireDataChangedEvent();
        }
        return reply;
    }

    public boolean setPaymentMethod(PaymentMethod paymentMethod){
        this.paymentMethod = paymentMethod;
        return true;
    }
    
    public boolean flushBufferedSales(){
       Order order = this.productBuffer.makeOrder(paymentMethod);
       
       if(order.getSales().size()>0) {
           this.apply(order);
           this.logger.log(order);
    
           this.productBuffer = new ProductBuffer( );
           paymentMethod = null;
    
           this.ioHandler.write(this.products);
           this.dataEventFirerer.fireDataChangedEvent( );
       }
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
    
    public PaymentMethod getPaymentMethod ( ) {
        return paymentMethod;
    }
    
    @Override
    public void replaceProduct (IProduct toReplace, IProduct with) {
        for(IProduct product : new ArrayList<>(this.products)){
            if(product.isSameProduct(toReplace)){
                int index = this.products.indexOf(toReplace);
                this.products.remove(toReplace);
                this.products.add(index, with);
            }else if(product.isComposedOf(toReplace)){
                product.replaceComponent(toReplace, with);
            }
        }
        this.dataEventFirerer.fireDataChangedEvent();
    }
    
    @Override
    public void addProduct (IProduct toAdd) {
        this.products.add(toAdd);
        this.dataEventFirerer.fireDataChangedEvent();
    }
    
    @Override
    public void removeProduct (IProduct toRemove) {
        for(IProduct product : new ArrayList<>(this.products)){
            if(product.isSameProduct(toRemove)){
                this.products.remove(product);
            }else if(product.isComposedOf(toRemove)){
                product.removeComponent(toRemove);
            }
        }
        this.dataEventFirerer.fireDataChangedEvent();
    }
}
