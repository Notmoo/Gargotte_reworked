package com.cess.gargotte.core.order_logging.factory;

/**
 * Created by Guillaume on 27/02/2017.
 */
public class SimpleIOFactoryParameters implements IIOFactoryParameters {
    
    private static final String FIELD_SEPARATOR = "\t", SALE_SEPARATOR = "\n", ORDER_SEPARATOR = "-----", SALE_PREFIX = " -> ";
    
    
    @Override
    public String getFieldSeparator () {
        return FIELD_SEPARATOR;
    }
    
    @Override
    public String getSaleSeparator () {
        return SALE_SEPARATOR;
    }
    
    @Override
    public String getSalePrefix () {
        return SALE_PREFIX;
    }
    
    @Override
    public String getOrderSeparator () {
        return ORDER_SEPARATOR;
    }
}
