package xyz.vladkozlov.pinguu;

import xyz.vladkozlov.pinguu.events.PingEventListener;
import xyz.vladkozlov.pinguu.events.PingEventType;
import xyz.vladkozlov.pinguu.processors.MacPingProcessor;
import xyz.vladkozlov.pinguu.processors.Processor;
import xyz.vladkozlov.pinguu.processors.WindowsPingProcessor;

import java.io.IOException;
import java.net.URL;

public class Ping {
    private final PingProcess pingProcess;
    private final Processor pingProcessor;

    public Ping(URL host) {
        this.pingProcess = new PingProcess(host);
        this.pingProcessor = getOsSpecificProcessorStrategy(pingProcess.getOS());
        assert pingProcessor != null;
    }

    public void addListener(PingEventType eventType, PingEventListener eventListener) {
        this.pingProcessor.subscribe(eventType, eventListener);
    }

    public void removeListener(PingEventType eventType, PingEventListener eventListener) {
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
