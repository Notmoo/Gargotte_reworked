package com.cess.gargotte.log;

import com.cess.gargotte.core.model.products.IProduct;
import com.cess.gargotte.core.model.sales.Order;
import com.cess.gargotte.log.factory.IIOFactoryParameters;
import com.cess.gargotte.log.factory.SimpleIOFactoryParameters;
import com.cess.gargotte.log.factory.SimpleIOLogFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guillaume on 25/02/2017.
 */
public final class GargotteIOLogHandlerService implements IIOLogHandler{
    
    private static GargotteIOLogHandlerService INSTANCE = null;
    
    private final Path path;
    private final SimpleIOLogFactory factory;
    private final IIOFactoryParameters params;
    
    private GargotteIOLogHandlerService (final String filePathStr) throws IOException {
        path = Paths.get(filePathStr);
        if(!Files.exists(path))
            Files.createFile(path);
        params = new SimpleIOFactoryParameters();
        factory = new SimpleIOLogFactory(params);
    }
    
    public static GargotteIOLogHandlerService getInstance() throws IOException{
        if(INSTANCE == null){
            INSTANCE = new GargotteIOLogHandlerService("ventes.log");
        }
        return INSTANCE;
    }
    
    @Override
    public boolean write (final Order order) {
        boolean success;
        try (FileWriter fw = new FileWriter(path.toString(), true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
        
            out.append(factory.newOrderWriter().write(order, factory.newSaleWriter()));
            success = true;
        }catch(FileNotFoundException e){
            try{
                Files.createFile(path);
                success = write(order);
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
    
    @Override
    public long count () throws IOException {
        long counter = 0L;
        try (FileReader fr = new FileReader(path.toString());
             BufferedReader br = new BufferedReader(fr)) {
            
            try{
                String temp;
                while((temp = br.readLine())!=null){
                    if(temp.equals(params.getOrderSeparator()))
                        counter++;
                }
            }catch(EOFException e){
            }
        }catch(FileNotFoundException e){
            throw new NoSuchFileException(path.toString());
        }
        return counter;
    }
    
    @Override
    public List<Order> read (final List<IProduct> products) throws IOException {
        return read(products, count());
    }
    
    @Override
    public List<Order> read (final List<IProduct> products, final long number)  throws IOException {
        List<Order> orders = new ArrayList<>();
        try (FileReader fr = new FileReader(path.toString());
             BufferedReader br = new BufferedReader(fr)) {
        
            try{
                StringBuilder sb = new StringBuilder();
                String temp;
                while((temp = br.readLine())!=null){
                    if(temp.equals(params.getOrderSeparator())){
                        orders.add(factory.newOrderReader().read(sb.toString(), factory.newSaleReader(), products));
                        if(orders.size()> number){
                            orders.remove(0);
                        }
                        sb = new StringBuilder();
                    }else{
                        sb.append(temp);
                    }
                }
            }catch(EOFException e){
            }
        }catch(FileNotFoundException e){
            throw new NoSuchFileException(path.toString());
        }
        return orders;
    }
}
