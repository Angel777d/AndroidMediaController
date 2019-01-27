package ru.angelovich.events;

public interface IEvent {
    String getName();

    IEventDispatcher getTarget();

    void setTarget(IEventDispatcher target);
}
