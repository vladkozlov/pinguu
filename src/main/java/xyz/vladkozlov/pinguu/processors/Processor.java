package xyz.vladkozlov.pinguu.processors;

import xyz.vladkozlov.pinguu.events.PingEventListener;
import xyz.vladkozlov.pinguu.events.PingEventType;

import java.io.BufferedReader;
import java.io.IOException;

public interface Processor {
    void process(BufferedReader inputStream) throws IOException;
    void subscribe(PingEventType event, PingEventListener listener);
    void unsubscribe(PingEventType event, PingEventListener listener);
}
