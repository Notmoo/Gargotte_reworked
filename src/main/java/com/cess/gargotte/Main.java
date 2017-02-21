package com.cess.gargotte;

import com.cess.gargotte.core.model.GargotteModel;
import com.cess.gargotte.core.model.products.ComposedProduct;
import com.cess.gargotte.core.model.products.IProduct;
import com.cess.gargotte.core.model.products.SimpleProduct;
import com.cess.gargotte.reader.IIOHandler;
import com.cess.gargotte.reader.SerIOHandler;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guillaume on 21/02/2017.
 */
public class Main {

    public static final void main(String[] args){
        GargotteModel model = new GargotteModel();
    }

    /**
     * Méthode destiné au test du programme
     * TODO à retirer
     * @param products
     */
    private static void printProducts(List<IProduct> products){
        System.out.println("------------------------------------------");
        for(IProduct product : products){
            System.out.println(product.getRepresentation(0));
        }
    }
}
