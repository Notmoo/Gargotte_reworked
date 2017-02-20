package com.cess.gargotte.log;

import com.cess.gargotte.core.model.sales.OrderBuilder;

/**
 * Created by Guillaume on 19/02/2017.
 */
public interface ILogger {

    public boolean log(OrderBuilder builder);
}
