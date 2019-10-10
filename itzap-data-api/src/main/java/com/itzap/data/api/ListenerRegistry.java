package com.itzap.data.api;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class ListenerRegistry {
    private final Map<String, Listener> registry = new ConcurrentHashMap<>();

    public void registerListener(Listener listener) {
        if (registry.containsKey(listener.getName())) {
            return;
        }

        registry.put(listener.getName(), listener);
    }

    public void unregisterListener(Listener listener) {
        registry.remove(listener.getName());
    }

    protected void fireEvent(Event event) {
        registry.values().forEach(l -> l.handle(event));
    }
}
