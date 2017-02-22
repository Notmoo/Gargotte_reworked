package com.cess.gargotte.log;

import com.cess.gargotte.core.model.sales.Order;
import com.cess.gargotte.core.model.sales.Sale;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by Guillaume on 19/02/2017.
 */
public class SimpleOrderLogger implements IOrderLogger {

    private final Path path;
    private final ISaleLogSyntax syntax;

    public SimpleOrderLogger (Path path, SaleLogSyntaxFactory syntaxFactory){
        this.path = path;
        this.syntax = syntaxFactory.newInlineSimpleSyntax();
    }

    public boolean log(Order order) {
        boolean success;
        try (FileWriter fw = new FileWriter(path.toString(), true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw)) {
                StringBuffer sb = new StringBuffer();

                for(Sale sale : order.getSales()) {
                    sb.append(syntax.applySyntax(sale, order.getPaymentMethod())).append("\n");
                }

                out.append(sb.toString());
                success = true;
            }catch(FileNotFoundException e){
                try{
                    Files.createFile(path);
                    success = log(order);
                }catch(IOException ioe){
                    ioe.printStackTrace();
                    success = false;
                }
            }catch(IOException ioe) {
                ioe.printStackTrace();
                success = false;
            }
        return success;
    }
}
