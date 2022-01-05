package xyz.vladkozlov.pinguu;

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
            pingApp.start();
        } catch (MalformedURLException e) {
            System.err.printf("Bad url %s, Err: %s", hostToPing, e.getMessage());
        }
    }
}
