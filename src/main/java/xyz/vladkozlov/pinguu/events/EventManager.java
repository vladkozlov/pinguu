package xyz.vladkozlov.pinguu.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventManager {
    private final Map<EventType, List<EventListener<Event<?>>>> listeners;

    public EventManager() {
        this.listeners = new HashMap<>();
    }

    public void subscribe(EventType eventType, EventListener<Event<?>> listener) {
        if (listeners.containsKey(eventType)) {
            listeners.get(eventType).add(listener);
        } else {
            ArrayList<EventListener<Event<?>>> list = new ArrayList<>();
            list.add(listener);
            listeners.put(eventType, list);
        }
    }

    public void unsubscribe(EventType eventType, EventListener<Event<?>> listener) {
        if (listeners.containsKey(eventType)) {
            listeners.get(eventType).remove(listener);
        }
    }

    public <T> void notify(EventType eventType, Event<T> data) {
        for (EventListener listener : this.listeners.get(eventType)) {
            listener.update(data);
        }
    }
}
