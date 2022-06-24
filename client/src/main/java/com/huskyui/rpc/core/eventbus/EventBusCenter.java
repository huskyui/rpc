package com.huskyui.rpc.core.eventbus;

import com.google.common.eventbus.EventBus;

/**
 * @author 王鹏
 */
public class EventBusCenter {
    private static final EventBus eventBus = new EventBus();

    private EventBusCenter(){

    }

    public static EventBus getInstance(){
        return eventBus;
    }

    public static void register(Object obj){
        eventBus.register(obj);
    }

    public static void unregister(Object obj) {
        eventBus.unregister(obj);
    }

    public static void post(Object obj) {
        eventBus.post(obj);
    }
}
