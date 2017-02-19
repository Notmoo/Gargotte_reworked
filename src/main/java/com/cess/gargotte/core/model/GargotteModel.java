package com.cess.gargotte.core.model;

import com.cess.gargotte.core.model.products.IProduct;
import com.cess.gargotte.log.ILogger;
import com.cess.gargotte.log.SimpleLogger;
import com.cess.gargotte.reader.IIOHandler;
import com.cess.gargotte.reader.SerIOHandler;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by Guillaume on 19/02/2017.
 */
public class GargotteModel {

    private static final Path path = Paths.get("taverne.gardata");

    private IIOHandler ioHandler;
    private ILogger logger;
    private List<IProduct> products;


    public GargotteModel(){
        ioHandler = new SerIOHandler();
        logger = new SimpleLogger();
        products = ioHandler.read();
    }
}
