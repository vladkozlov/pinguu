package xyz.vladkozlov.pinguu.processors;

import xyz.vladkozlov.pinguu.events.Event;
import xyz.vladkozlov.pinguu.events.EventListener;
import xyz.vladkozlov.pinguu.events.EventManager;
import xyz.vladkozlov.pinguu.events.EventType;

public abstract class PingProcessor {
    private final EventManager events;

    public PingProcessor() {
        events = new EventManager();
    }

    public <T> void notify(EventType event, Event<T> pingData) {
        events.notify(event, pingData);
    }

    public void subscribe(EventType event, EventListener listener) {
        events.subscribe(event, listener);
    }

    public void unsubscribe(EventType event, EventListener listener) {
        events.unsubscribe(event, listener);
    }
}
