package com.cess.gargotte.core.model;

import com.cess.gargotte.core.model.listeners.IModelFirerer;
import com.cess.gargotte.core.model.listeners.IModelListener;
import com.cess.gargotte.core.model.listeners.ModelFirerer;
import com.cess.gargotte.core.model.products.*;
import com.cess.gargotte.core.model.sales.Order;
import com.cess.gargotte.core.model.sales.PaymentMethod;
import com.cess.gargotte.core.model.sales.ProductBuffer;
import com.cess.gargotte.core.model.sales.Sale;
import com.cess.gargotte.core.order_logging.GargotteOrderLoggingService;
import com.cess.gargotte.core.order_logging.IOrderLoggingHandler;
import com.cess.gargotte.core.src_file_handler.ISrcFileHandler;
import com.cess.gargotte.core.src_file_handler.SerSrcFileHandler;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guillaume on 19/02/2017.
 */
public class GargotteModel implements IModel{

    private static final Path PRODUCT_FILE_PATH = Paths.get("produits.gargotte"), SALES_LOG_FILE_PATH = Paths.get("ventes.write");

    private ISrcFileHandler ioHandler;
    private IOrderLoggingHandler logger;
    
    private List<IProduct> products;
    
    private PaymentMethod paymentMethod;
    private ProductBuffer productBuffer;

    private IModelFirerer dataEventFirerer;
    private IModelFirerer modelStateFirerer;


    public GargotteModel() throws IOException {
        ioHandler = new SerSrcFileHandler(PRODUCT_FILE_PATH);
        logger = GargotteOrderLoggingService.getInstance();
        productBuffer = new ProductBuffer();

        initProductList();

        dataEventFirerer = new ModelFirerer();
        modelStateFirerer = new ModelFirerer();
    }

    private void initProductList() {
        products = ioHandler.read();
    }

    public List<IReadOnlyProduct> getProducts() {
        List<IReadOnlyProduct> reply = new ArrayList<>();
        for(IProduct product : products){
            reply.add(this.toReadOnlyProduct(product));
        }
        return reply;
    }

    public List<IReadOnlyProduct> getProductsFromCat(final String cat){
        final List<IReadOnlyProduct> reply = new ArrayList<>( );
        if(cat!=null ) {
            this.products.stream( ).filter((product) -> product.getCat( ).equals(cat)).forEach((product) -> reply.add(this.toReadOnlyProduct(product)));
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

    public boolean bufferSale(IReadOnlyProduct product){
        boolean reply = productBuffer.addProduct(product);
        if(reply){
            this.dataEventFirerer.fireDataChangedEvent();
        }
        return reply;
    }

    public boolean unbufferSale(IReadOnlyProduct product){
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
           this.logger.write(order);
    
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
                this.products.stream()
                             .filter(product->toReadOnlyProduct(product).isSameProduct(sale.getProduct()))
                             .forEach(product->product.applySale(sale.getAmount()));
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
    
    private void applyChanges(){
        this.ioHandler.write(this.products);
        this.dataEventFirerer.fireDataChangedEvent();
    }
    
    @Override
    public void replaceProduct (IReadOnlyProduct toReplace, IReadOnlyProduct with) {
        IProduct toReplaceEditable = toEditableProduct(toReplace);
        IProduct withEditable = toEditableProduct(with);
        int index = 0;
        for(IProduct product : new ArrayList<>(this.products)){
            if(product.isSameProduct(toReplaceEditable)){
                this.products.remove(toReplaceEditable);
                this.products.add(index, withEditable);
            }else if(product.isComposedOf(toReplaceEditable)){
                product.replaceComponent(toReplaceEditable, withEditable);
            }
            index++;
        }
        
        applyChanges();
    }
    
    @Override
    public void addProduct (IReadOnlyProduct toAdd) {
        this.products.add(this.toEditableProduct(toAdd));
        
        applyChanges();
    }
    
    @Override
    public void removeProduct (IReadOnlyProduct toRemove) {
        for(IProduct product : new ArrayList<>(this.products)){
            if(toRemove.isSameProduct(this.toReadOnlyProduct(product))){
                this.products.remove(product);
            }else if(toRemove.isComposedOf(this.toReadOnlyProduct(product))){
                product.removeComponent(this.toEditableProduct(toRemove));
            }
        }
    
        applyChanges();
    }
    
    @Override
    public Path getRessourceFilePath () {
        return PRODUCT_FILE_PATH;
    }
    
    @Override
    public Path getOrderLogFilePath () {
        return SALES_LOG_FILE_PATH;
    }
    
    private IProduct toEditableProduct(IReadOnlyProduct roProduct){
        //TODO à refaire pour permettre aux nouveaux produits d'êtres ajoutés
        if(roProduct!=null) {
            for (IProduct product : this.products) {
                if (roProduct.getName().equals(product.getName())
                    && roProduct.getCat().equals(product.getCat())
                    && roProduct.getPrice() == product.getPrice()) {
                    return product;
                }
            }
        }
        return null;
    }
    
    private IReadOnlyProduct toReadOnlyProduct(IProduct product){
        if(product!=null) {
            if (product.getClass().equals(SimpleProduct.class)) {
                return new SimpleReadOnlyProduct(product.getName(), product.getCat(), product.getPrice(),
                                                 product.getAmountRemaining(), product.getAmountSold());
            } else if (product.getClass().equals(ComposedProduct.class)) {
                List<IReadOnlyProduct> components = new ArrayList<>();
                for (IProduct component : ((ComposedProduct) product).getComponents()) {
                    components.add(toReadOnlyProduct(component));
                }
                return new ComposedReadOnlyProduct(product.getName(), product.getCat(), product.getPrice(),
                                                   product.getAmountSold(), components);
            } else {
                throw new IllegalStateException("Type de produit inconnu : " + product.getClass().toString());
            }
        }else{
            System.out.println("product null");
            return null;
        }
    }
}
