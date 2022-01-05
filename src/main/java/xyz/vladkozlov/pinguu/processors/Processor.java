package xyz.vladkozlov.pinguu.processors;

import xyz.vladkozlov.pinguu.events.EventListener;
import xyz.vladkozlov.pinguu.events.EventType;

import java.io.BufferedReader;
import java.io.IOException;

public interface Processor {
    void process(BufferedReader inputStream) throws IOException;
    void subscribe(EventType event, EventListener listener);
    void unsubscribe(EventType event, EventListener listener);
}
