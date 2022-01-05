package xyz.vladkozlov.pinguu.listeners;

import xyz.vladkozlov.pinguu.PingException;
import xyz.vladkozlov.pinguu.events.Event;
import xyz.vladkozlov.pinguu.events.EventListener;

public class ExceptionListener implements EventListener<PingException> {
    @Override
    public void update(Event<PingException> data) {
        var ex = data.data();
        System.err.printf("[%d] Exception occurred: %s\n", data.id(), ex.getMessage());
    }
}
