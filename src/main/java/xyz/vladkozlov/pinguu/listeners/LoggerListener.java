package xyz.vladkozlov.pinguu.listeners;

import xyz.vladkozlov.pinguu.PingData;
import xyz.vladkozlov.pinguu.events.Event;
import xyz.vladkozlov.pinguu.events.EventListener;

public class LoggerListener implements EventListener<PingData> {

    @Override
    public void update(Event<PingData> event) {
        var pingData = event.data();
        System.out.printf("[%d] ip: %s, time: %s\n", event.id(), pingData.ip(), pingData.time());
    }

}
