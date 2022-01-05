package xyz.vladkozlov.pinguu;

import xyz.vladkozlov.pinguu.events.EventType;
import xyz.vladkozlov.pinguu.listeners.ExceptionListener;
import xyz.vladkozlov.pinguu.listeners.LoggerListener;
import xyz.vladkozlov.pinguu.listeners.StatisticsListener;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Application {
    public static void main(String[] args) throws IOException {
        String hostToPing = "https://google.com";
        URL url;
        try {
            url = new URL(hostToPing);

            var pingApp = new Pinguu(url);

            pingApp.addListener(EventType.PING_EVENT, new LoggerListener());
            pingApp.addListener(EventType.PING_EVENT, new StatisticsListener());
            pingApp.addListener(EventType.EXCEPTION_EVENT, new ExceptionListener());
//            var cl = new CombinedListener();
//            pingApp.addListener(EventType.PING_EVENT, cl);
//            pingApp.addListener(EventType.EXCEPTION_EVENT, cl);
            pingApp.start();
        } catch (MalformedURLException e) {
            System.err.printf("Bad url %s, Err: %s", hostToPing, e.getMessage());
        }
    }
}
