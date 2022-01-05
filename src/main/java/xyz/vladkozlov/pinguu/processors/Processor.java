package xyz.vladkozlov.pinguu.processors;

import xyz.vladkozlov.pinguu.PingData;
import xyz.vladkozlov.pinguu.events.EventListener;
import xyz.vladkozlov.pinguu.events.EventType;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public interface Processor {
    void process(BufferedReader inputStream) throws IOException;
    Map<Function<String, Boolean>, String> rules();
    Optional<PingData> parsePingStringToPingData(String pingString);

    void subscribe(EventType event, EventListener listener);
    void unsubscribe(EventType event, EventListener listener);
}
