package com.cess.gargotte.reader;

import com.cess.gargotte.core.model.products.IProduct;

import java.nio.file.Path;
import java.util.List;

/**
 * Created by Guillaume on 19/02/2017.
 */
public class SerIOHandler implements IIOHandler{

    public SerIOHandler(Path path){
        
    }

    public List<IProduct> read() {
        return null;
    }

    public boolean write(List<IProduct> products) {
        return false;
    }
}
