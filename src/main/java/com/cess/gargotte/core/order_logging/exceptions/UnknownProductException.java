package com.cess.gargotte.core.order_logging.exceptions;

/**
 * Created by Guillaume on 27/02/2017.
 */
public class UnknownProductException extends Exception {
    
    public UnknownProductException(){
        super();
    }
    
    public UnknownProductException(String message){
        super(message);
    }
}
