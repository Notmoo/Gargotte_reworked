package com.cess.gargotte.core.model.listeners;

import java.util.EventListener;

/**
 * Created by Guillaume on 19/02/2017.
 */
public interface IModelListener extends EventListener{
    public void onDataChangedEvent();
    public void onErrorEvent(Throwable e);
}
