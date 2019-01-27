package ru.angelovich.events;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EventDispatcher implements IEventDispatcher {
    private Map<String, Set<IEventCallback>> events = new HashMap<>();

    public void addEventListener(String eventName, IEventCallback callback) {
        Set<IEventCallback> callbacks;
        if (!events.containsKey(eventName)) {
            callbacks = new HashSet<>();
            events.put(eventName, callbacks);
        } else {
            callbacks = events.get(eventName);
        }
        if (callbacks != null)
            callbacks.add(callback);
    }

    public void removeEventListener(String eventName, IEventCallback callback) {
        Set<IEventCallback> callbacks = events.containsKey(eventName) ? events.get(eventName) : events.put(eventName, new HashSet<IEventCallback>());
        if (callbacks != null)
            callbacks.remove(callback);
    }

    public void dispatch(IEvent event) {
        event.setTarget(this);
        Set<IEventCallback> callbacks = events.get(event.getName());
        if (callbacks != null) {
            for (IEventCallback callback : callbacks) {
                callback.call(event);
            }
        }
    }
}
