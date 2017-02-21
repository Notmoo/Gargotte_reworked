package com.cess.gargotte.core.model.listeners;

import javax.swing.event.EventListenerList;

/**
 * Created by Guillaume on 19/02/2017.
 */
public class ModelFirerer implements IModelFirerer {

    private EventListenerList listeners;

    public ModelFirerer(){
        listeners = new EventListenerList();
    }

    public void addListener(IModelListener l) {
        this.listeners.add(IModelListener.class, l);
    }

    public void removeListener(IModelListener l) {
        this.listeners.remove(IModelListener.class, l);
    }

    public void fireDataChangedEvent() {
        for(IModelListener iml : getModelListener()){
            iml.onDataChangedEvent();
        }
    }

    public void fireErrorEvent(Throwable e) {
        for(IModelListener iml : getModelListener()){
            iml.onErrorEvent(e);
        }
    }

    private IModelListener[] getModelListener(){
        return this.listeners.getListeners(IModelListener.class);
    }
}
