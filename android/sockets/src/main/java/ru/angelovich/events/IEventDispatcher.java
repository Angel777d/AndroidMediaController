package ru.angelovich.events;

public interface IEventDispatcher {
    void addEventListener(String eventName, IEventCallback callback);

    void removeEventListener(String eventName, IEventCallback callback);

    void dispatch(IEvent event);
}
