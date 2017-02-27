package com.cess.gargotte.log.factory.log;

import com.cess.gargotte.core.model.sales.Sale;

/**
 * Created by Guillaume on 21/02/2017.
 */
public interface ISaleLogWriter {
    String write(Sale sale);
}
