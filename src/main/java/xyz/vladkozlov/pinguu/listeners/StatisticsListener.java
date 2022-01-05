package xyz.vladkozlov.pinguu.listeners;

import xyz.vladkozlov.pinguu.PingData;
import xyz.vladkozlov.pinguu.events.Event;
import xyz.vladkozlov.pinguu.events.EventListener;

public class StatisticsListener implements EventListener<PingData> {
    private long eventCounter;
    private int averageTime;
    private int max;
    private int min = Integer.MAX_VALUE;

    @Override
    public void update(Event<PingData> event) {
        var pingData = event.data();
        if (min > pingData.time()) min = pingData.time();

        if (max < pingData.time()) max = pingData.time();


        averageTime += pingData.time();
        eventCounter++;

        System.out.printf("[%d] min %s, avg: %s, max: %s, #samples: %s\n", event.id(), min, averageTime/eventCounter, max, eventCounter);
    }
}
