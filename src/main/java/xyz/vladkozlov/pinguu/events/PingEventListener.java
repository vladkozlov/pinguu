package xyz.vladkozlov.pinguu.events;

import xyz.vladkozlov.pinguu.PingData;

public interface PingEventListener {
    void update(PingData pingData);
}
