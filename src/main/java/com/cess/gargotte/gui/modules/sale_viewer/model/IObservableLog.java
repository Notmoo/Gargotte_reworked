package com.cess.gargotte.gui.modules.sale_viewer.model;

import javafx.beans.property.StringProperty;

import java.util.List;

/**
 * Created by Guillaume on 25/02/2017.
 */
public interface IObservableLog {
    
    StringProperty name();
    StringProperty timeStamp();
    StringProperty amount();
    StringProperty price();
    StringProperty paymentMethod();
    
    List<IObservableLog> content();
}
