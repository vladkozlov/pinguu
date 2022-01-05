package xyz.vladkozlov.pinguu.processors;

import xyz.vladkozlov.pinguu.PingData;
import xyz.vladkozlov.pinguu.PingException;
import xyz.vladkozlov.pinguu.events.Event;
import xyz.vladkozlov.pinguu.events.EventListener;
import xyz.vladkozlov.pinguu.events.EventManager;
import xyz.vladkozlov.pinguu.events.EventType;

import java.io.BufferedReader;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public abstract class PingProcessor implements Processor {
    private final EventManager events;
    private long eventId;

    public PingProcessor() {
        events = new EventManager();
    }

    @Override
    public void process(BufferedReader inputStream) {
        inputStream
                .lines()
                .map(inputString -> new Combinator(parsePingStringToPingData(inputString), inputString))
                .forEach(combinator ->
                        combinator
                                .pingData()
                                .ifPresentOrElse(pd -> notify(EventType.PING_EVENT, generateEvent(pd)),
                                        () -> rules()
                                            .entrySet()
                                            .stream()
                                            .filter(entry -> entry.getKey().apply(combinator.origStr()))
                                            .findFirst()
                                            .ifPresent(entry ->
                                                    notify(EventType.EXCEPTION_EVENT,
                                                            generateEvent(new PingException(entry.getValue()))))));
    }

    record Combinator(Optional<PingData> pingData, String origStr){}

    private <T> Event<T> generateEvent(T data) {
        return new Event<>(++eventId, LocalDateTime.now(), data);
    }

    public Optional<PingData> parsePingStringToPingData(String pingString) {
        return Optional.of(new PingData("ipString", 0, 0, 0));
    }

    public Map<Function<String, Boolean>, String> rules() {
        return new HashMap<>();
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
