package xyz.vladkozlov.pinguu.events;

public interface EventListener<T> {
    void update(Event<T> event);
}
