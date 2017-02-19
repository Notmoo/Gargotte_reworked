package com.cess.gargotte.reader;

import com.cess.gargotte.core.model.products.IProduct;

import java.util.List;

/**
 * Created by Guillaume on 15/02/2017.
 */
public interface IIOHandler {

    public List<IProduct> read();
    public boolean write(List<IProduct> products);
}
