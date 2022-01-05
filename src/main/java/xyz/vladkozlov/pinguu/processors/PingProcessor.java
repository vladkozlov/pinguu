package xyz.vladkozlov.pinguu.processors;

import xyz.vladkozlov.pinguu.PingData;
import xyz.vladkozlov.pinguu.events.PingEventListener;
import xyz.vladkozlov.pinguu.events.PingEventManager;
import xyz.vladkozlov.pinguu.events.PingEventType;

public abstract class PingProcessor {
    private final PingEventManager events;

    public PingProcessor() {
        events = new PingEventManager();
    }

    public void notify(PingEventType event, PingData pingData) {
        events.notify(event, pingData);
    }

    public void subscribe(PingEventType event, PingEventListener listener) {
        events.subscribe(event, listener);
    }

    public void unsubscribe(PingEventType event, PingEventListener listener) {
        events.unsubscribe(event, listener);
    }
}
