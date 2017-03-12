package com.cess.gargotte.core.order_logging.factory.log;

import com.cess.gargotte.core.model.products.IReadOnlyProduct;
import com.cess.gargotte.core.model.sales.Order;

import java.util.List;

/**
 * Created by Guillaume on 27/02/2017.
 */
public interface IOrderLogReader {
    
    Order read(String str, ISaleLogReader saleReader, List<IReadOnlyProduct> products);
}
