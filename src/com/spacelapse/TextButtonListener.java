package com.spacelapse;

import java.util.EventListener;
import java.util.EventObject;

import javax.swing.event.EventListenerList;

class ClickEvent extends EventObject {
    public ClickEvent(Object source) {
        super(source);
    }
}

interface ClickEventListener extends EventListener {
    public void myEventOccurred(ClickEvent evt);
}