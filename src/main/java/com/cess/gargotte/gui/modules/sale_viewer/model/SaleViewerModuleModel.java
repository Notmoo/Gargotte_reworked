package com.cess.gargotte.gui.modules.sale_viewer.model;

import com.cess.gargotte.core.model.IModel;
import com.cess.gargotte.core.model.sales.Order;
import com.cess.gargotte.core.model.sales.Sale;
import com.cess.gargotte.core.order_logging.GargotteOrderLoggingService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guillaume on 27/02/2017.
 */
public class SaleViewerModuleModel {
    
    private IModel model;
    
    public SaleViewerModuleModel (final IModel model) {
        this.model = model;
    }
    
    public List<IObservableLog> getLastOrders (final int number) {
        try {
            List<Order> orders = GargotteOrderLoggingService.getInstance().read(model.getProducts(), number);
            
            List<IObservableLog> reply = new ArrayList<>();
            for(Order order : orders){
                List<IObservableLog> sales = new ArrayList<>();
                for(Sale sale : order.getSales()){
                    sales.add(new ObservableSaleLog(sale.getProduct(), sale.getAmount(), sale.getPrice()));
                }
                reply.add(new ObservableOrderLog(sales, order.getTimeStamp(), order.getTotalPrice(), order.getPaymentMethod()));
            }
            
            return reply;
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }
}
