package xyz.vladkozlov.pinguu.events;

import xyz.vladkozlov.pinguu.PingData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PingEventManager {
    private final Map<PingEventType, List<PingEventListener>> listeners;

    public PingEventManager() {
        this.listeners = new HashMap<>();
    }

    public void subscribe(PingEventType eventType, PingEventListener listener) {
        if (listeners.containsKey(eventType)) {
            listeners.get(eventType).add(listener);
        } else {
            var list = new ArrayList<PingEventListener>();
            list.add(listener);
            listeners.put(eventType, list);
        }
    }

    public void unsubscribe(PingEventType eventType, PingEventListener listener) {
        if (listeners.containsKey(eventType)) {
            listeners.get(eventType).remove(listener);
        }
    }

    public void notify(PingEventType eventType, PingData data) {
        for (PingEventListener listener : this.listeners.get(eventType)) {
            listener.update(data);
        }
    }
}
