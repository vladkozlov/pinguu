package xyz.vladkozlov.pinguu;

import xyz.vladkozlov.pinguu.events.PingEventType;
import xyz.vladkozlov.pinguu.listeners.LoggerPingListener;
import xyz.vladkozlov.pinguu.listeners.StatisticsPingListener;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Application {
    public static void main(String[] args) throws IOException {
        String hostToPing = "https://ya.ru";
        URL url;
        try {
            url = new URL(hostToPing);

            var pingApp = new Ping(url);
            pingApp.addListener(PingEventType.PING_EVENT, new LoggerPingListener());
            pingApp.addListener(PingEventType.PING_EVENT, new StatisticsPingListener());
            pingApp.start();
        } catch (MalformedURLException e) {
            System.err.printf("Bad url %s, Err: %s", hostToPing, e.getMessage());
        }
    }
}
