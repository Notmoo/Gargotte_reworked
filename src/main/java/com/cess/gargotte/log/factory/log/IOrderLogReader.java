package com.cess.gargotte.log.factory.log;

import com.cess.gargotte.core.model.products.IProduct;
import com.cess.gargotte.core.model.sales.Order;

import java.util.List;

/**
 * Created by Guillaume on 27/02/2017.
 */
public interface IOrderLogReader {
    
    Order read(String str, ISaleLogReader saleReader, List<IProduct> products);
}
