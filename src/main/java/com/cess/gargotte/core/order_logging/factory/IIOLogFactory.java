package com.cess.gargotte.core.order_logging.factory;

import com.cess.gargotte.core.order_logging.factory.log.IOrderLogReader;
import com.cess.gargotte.core.order_logging.factory.log.IOrderLogWriter;
import com.cess.gargotte.core.order_logging.factory.log.ISaleLogReader;
import com.cess.gargotte.core.order_logging.factory.log.ISaleLogWriter;

/**
 * Created by Guillaume on 27/02/2017.
 */
public interface IIOLogFactory {
    
    ISaleLogWriter newSaleWriter ();
    
    ISaleLogReader newSaleReader ();
    
    IOrderLogWriter newOrderWriter ();
    
    IOrderLogReader newOrderReader ();
}
