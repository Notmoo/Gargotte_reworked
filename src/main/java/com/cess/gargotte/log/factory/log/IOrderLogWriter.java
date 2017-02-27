package com.cess.gargotte.log.factory.log;

import com.cess.gargotte.core.model.sales.Order;

/**
 * Created by Guillaume on 27/02/2017.
 */
public interface IOrderLogWriter {
    
    String write(Order order, ISaleLogWriter saleWriter);
}
