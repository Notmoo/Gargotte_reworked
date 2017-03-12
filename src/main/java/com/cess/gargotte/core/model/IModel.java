package com.cess.gargotte.core.model;

import com.cess.gargotte.core.model.listeners.IModelListener;
import com.cess.gargotte.core.model.products.IReadOnlyProduct;
import com.cess.gargotte.core.model.sales.Order;
import com.cess.gargotte.core.model.sales.PaymentMethod;

import java.nio.file.Path;
import java.util.List;

/**
 * Created by Guillaume on 22/02/2017.
 */
public interface IModel {
    List<IReadOnlyProduct> getProducts();
    List<IReadOnlyProduct> getProductsFromCat(final String cat);
    Order getCurrentOrder();
    List<String> getCatList();
    PaymentMethod getPaymentMethod ( );
    
    void addDataListener(IModelListener l);
    void removeDataListener(IModelListener l);
    void addStateListener(IModelListener l);
    void removeStateListener(IModelListener l);
    
    boolean setPaymentMethod(PaymentMethod paymentMethod);
    
    boolean bufferSale(IReadOnlyProduct product);
    boolean unbufferSale(IReadOnlyProduct product);
    boolean flushBufferedSales();
    
    void replaceProduct (IReadOnlyProduct toReplace, IReadOnlyProduct with);
    void addProduct(IReadOnlyProduct toAdd);
    void removeProduct(IReadOnlyProduct toRemove);
    
    Path getRessourceFilePath();
    Path getOrderLogFilePath();
}
