package com.cess.gargotte.log;

import com.cess.gargotte.core.model.sales.Order;

/**
 * Created by Guillaume on 19/02/2017.
 */
public interface IOrderLogger {

    public boolean log(Order order);
}
