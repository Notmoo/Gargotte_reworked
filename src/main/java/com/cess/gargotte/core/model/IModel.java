package com.cess.gargotte.core.model;

import com.cess.gargotte.core.model.listeners.IModelListener;
import com.cess.gargotte.core.model.products.IProduct;
import com.cess.gargotte.core.model.sales.Order;
import com.cess.gargotte.core.model.sales.PaymentMethod;

import java.nio.file.Path;
import java.util.List;

/**
 * Created by Guillaume on 22/02/2017.
 */
public interface IModel {
    List<IProduct> getProducts();
    List<IProduct> getProductsFromCat(final String cat);
    Order getCurrentOrder();
    List<String> getCatList();
    PaymentMethod getPaymentMethod ( );
    
    void addDataListener(IModelListener l);
    void removeDataListener(IModelListener l);
    void addStateListener(IModelListener l);
    void removeStateListener(IModelListener l);
    
    boolean setPaymentMethod(PaymentMethod paymentMethod);
    
    boolean bufferSale(IProduct product);
    boolean unbufferSale(IProduct product);
    boolean flushBufferedSales();
    
    void replaceProduct (IProduct toReplace, IProduct with);
    void addProduct(IProduct toAdd);
    void removeProduct(IProduct toRemove);
    
    Path getRessourceFilePath();
    Path getOrderLogFilePath();
}
