package xyz.vladkozlov.pinguu.listeners;

import xyz.vladkozlov.pinguu.PingException;
import xyz.vladkozlov.pinguu.events.Event;
import xyz.vladkozlov.pinguu.events.EventListener;

public class ExceptionListener implements EventListener<PingException> {
    @Override
    public void update(Event<PingException> event) {
        var ex = event.data();
        System.err.printf("[%d] Exception occurred: %s\n", event.id(), ex.getMessage());
    }
}
