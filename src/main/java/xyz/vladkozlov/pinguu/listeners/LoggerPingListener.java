package xyz.vladkozlov.pinguu.listeners;

import xyz.vladkozlov.pinguu.PingData;
import xyz.vladkozlov.pinguu.events.PingEventListener;

public class LoggerPingListener implements PingEventListener {
    private long counter;

    @Override
    public void update(PingData pingData) {
        counter++;
        System.out.printf("[%s] ip: %s, time: %s\n", counter, pingData.ip(), pingData.time());
    }
}
