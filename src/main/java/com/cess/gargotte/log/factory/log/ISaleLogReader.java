package com.cess.gargotte.log.factory.log;

import com.cess.gargotte.core.model.products.IProduct;
import com.cess.gargotte.core.model.sales.Sale;
import com.cess.gargotte.log.exceptions.UnknownProductException;

import java.util.List;

/**
 * Created by Guillaume on 27/02/2017.
 */
public interface ISaleLogReader {
    Sale read(String str, List<IProduct> products) throws UnknownProductException;
}
