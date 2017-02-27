package com.cess.gargotte.core.src_file_handler;

import com.cess.gargotte.core.model.products.IProduct;

import java.util.List;

/**
 * Created by Guillaume on 15/02/2017.
 */
public interface ISrcFileHandler {

    public List<IProduct> read();
    public boolean write(List<IProduct> products);
}
