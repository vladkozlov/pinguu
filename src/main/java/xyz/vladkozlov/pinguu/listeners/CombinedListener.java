package xyz.vladkozlov.pinguu.listeners;

import xyz.vladkozlov.pinguu.PingData;
import xyz.vladkozlov.pinguu.PingException;
import xyz.vladkozlov.pinguu.events.Event;
import xyz.vladkozlov.pinguu.events.EventListener;

public class CombinedListener<T> implements EventListener<T> {
    @Override
    public void update(Event<T> event) {
        var data = event.data();

        if (data instanceof PingData pingData) {
            System.out.printf("Pingu is %s\n", pingData.time());
        } else if (data instanceof PingException pingException) {
            System.err.printf("Pingu is dead: %s", pingException.getMessage());
        }
    }
}
