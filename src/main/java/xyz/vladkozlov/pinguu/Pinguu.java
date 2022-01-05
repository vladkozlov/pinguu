package xyz.vladkozlov.pinguu;

import xyz.vladkozlov.pinguu.events.EventListener;
import xyz.vladkozlov.pinguu.events.EventType;
import xyz.vladkozlov.pinguu.processors.Processor;
import xyz.vladkozlov.pinguu.processors.mac.MacPingProcessor;
import xyz.vladkozlov.pinguu.processors.win.WindowsPingProcessor;

import java.io.IOException;
import java.net.URL;

public class Pinguu {
    private final PingProcess pingProcess;
    private final Processor pingProcessor;

    public Pinguu(URL host) {
        this.pingProcess = new PingProcess(host);
        this.pingProcessor = getOsSpecificProcessorStrategy(pingProcess.getOS());
        assert pingProcessor != null;
    }

    public void addListener(EventType eventType, EventListener eventListener) {
        this.pingProcessor.subscribe(eventType, eventListener);
    }

    public void removeListener(EventType eventType, EventListener eventListener) {
        this.pingProcessor.unsubscribe(eventType, eventListener);
    }

    public boolean start() throws IOException {
        pingProcess.start();
        var inputStream = pingProcess.getInputStream();

        pingProcessor.process(inputStream);
        return false;
    }

    private Processor getOsSpecificProcessorStrategy(OS os) {
        if (OS.MAC.equals(os)) {
            return new MacPingProcessor();
        } else if (OS.WIN.equals(os)) {
            return new WindowsPingProcessor();
        }

        return null;
    }

    public boolean stop()
    {
        return this.pingProcess.stop();
    }
}
