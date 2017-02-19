package com.cess.gargotte.core.model.listeners;

import javax.swing.event.EventListenerList;
import java.util.EventListener;

/**
 * Created by Guillaume on 19/02/2017.
 */
public class ModelFirerer implements IModelFirerer {

    private EventListenerList listeners;

    public void addListener(IModelListener l) {
        this.listeners.add(l.getClass(), l);
    }

    public void removeListener(IModelListener l) {
        this.listeners.remove(l.getClass(), l);
    }

    public void fireDataChangedEvent() {
        for(IModelListener iml : getModelListener()){
            iml.onDataChangedEvent();
        }
    }

    private IModelListener[] getModelListener(){
        return this.listeners.getListeners(IModelListener.class);
    }
}
