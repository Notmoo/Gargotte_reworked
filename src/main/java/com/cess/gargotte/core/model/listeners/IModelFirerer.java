package com.cess.gargotte.core.model.listeners;

/**
 * Created by Guillaume on 19/02/2017.
 */
public interface IModelFirerer {

    public void addListener(IModelListener l);
    public void removeListener(IModelListener l);
    public void fireDataChangedEvent();
}
