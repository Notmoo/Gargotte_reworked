package com.cess.gargotte.gui.modules.order.view;

import com.cess.gargotte.core.model.products.IProduct;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;

/**
 * Created by Guillaume on 21/02/2017.
 */
public class ListViewTab extends Tab{
    
    private ListView<IProduct> list;
    
    public ListViewTab (String name, ListView<IProduct> list){
        super(name);
        this.list = list;
        this.setContent(list);
    }
    
    public ListView<IProduct> getListView(){
        return list;
    }
}
