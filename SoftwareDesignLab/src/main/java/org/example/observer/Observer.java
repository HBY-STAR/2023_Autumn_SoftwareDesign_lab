package org.example.observer;

import org.example.invoker.Invoker;

public abstract class Observer {
    protected Invoker invoker;
    public abstract void update();

}
