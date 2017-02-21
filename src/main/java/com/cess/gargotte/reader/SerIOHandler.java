package com.cess.gargotte.reader;

import com.cess.gargotte.core.model.products.IProduct;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guillaume on 19/02/2017.
 */
public class SerIOHandler implements IIOHandler{

    private final Path path;

    public SerIOHandler(Path path){
        this.path = path;
    }

    public List<IProduct> read() {
        List<IProduct> products = new ArrayList<IProduct>();
        try ( final FileInputStream fichier = new FileInputStream(path.toFile());
              ObjectInputStream ois = new ObjectInputStream(fichier)){

            Object obj;
            while ((obj = ois.readObject()) != null) {
               if (obj instanceof IProduct) {
                  products.add((IProduct) obj);
               }
            }
        }catch(EOFException e){
            //Fin du fichier
            //Ce n'est pas un cas d'erreur
        }catch(FileNotFoundException e){
            try{
                Files.createFile(path );
                return read();
            }catch(IOException ioe){
                e.printStackTrace();
            }
        } catch (final java.io.IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return products;
    }

    public boolean write(List<IProduct> products) {
        boolean success = true;
        ObjectOutputStream oos = null;
        try {
            final FileOutputStream fichier = new FileOutputStream(path.toFile());
            oos = new ObjectOutputStream(fichier);

            for(IProduct product : products){
                oos.writeObject(product);
            }

        } catch (final java.io.IOException e) {
            e.printStackTrace();
            success = false;
        } finally {
            try {
                if (oos != null) {
                    oos.flush();
                    oos.close();
                }
            } catch (final IOException ex) {
                ex.printStackTrace();
            }
        }
        return success;
    }
}
