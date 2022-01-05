package xyz.vladkozlov.pinguu.listeners;

import xyz.vladkozlov.pinguu.PingData;
import xyz.vladkozlov.pinguu.events.PingEventListener;

public class StatisticsPingListener implements PingEventListener {
    private long eventCounter;
    private int averageTime;
    private int max;
    private int min = Integer.MAX_VALUE;

    @Override
    public void update(PingData pingData) {
        if (min > pingData.time()) min = pingData.time();

        if (max < pingData.time()) max = pingData.time();


        averageTime += pingData.time();
        eventCounter++;

        System.out.printf("min %s, avg: %s, max: %s, #samples: %s\n", min, averageTime/eventCounter, max, eventCounter);
    }
}
