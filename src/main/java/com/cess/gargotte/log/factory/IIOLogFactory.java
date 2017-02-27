package com.cess.gargotte.log.factory;

import com.cess.gargotte.log.factory.log.IOrderLogReader;
import com.cess.gargotte.log.factory.log.IOrderLogWriter;
import com.cess.gargotte.log.factory.log.ISaleLogReader;
import com.cess.gargotte.log.factory.log.ISaleLogWriter;

/**
 * Created by Guillaume on 27/02/2017.
 */
public interface IIOLogFactory {
    
    ISaleLogWriter newSaleWriter ();
    
    ISaleLogReader newSaleReader ();
    
    IOrderLogWriter newOrderWriter ();
    
    IOrderLogReader newOrderReader ();
}
