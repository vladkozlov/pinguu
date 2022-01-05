package xyz.vladkozlov.pinguu;

import xyz.vladkozlov.pinguu.processors.MacPingProcessor;
import xyz.vladkozlov.pinguu.processors.PingProcessor;
import xyz.vladkozlov.pinguu.processors.WindowsPingProcessor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class Ping {
    private final PingProcess pingProcess;

    public Ping(URL host) {
        this.pingProcess = new PingProcess(host);
    }

    public boolean start() throws IOException {
        pingProcess.start();

        PingProcessor pingProcessor = getOsSpecificProcessorStrategy(pingProcess.getOS());
        var inputStream = pingProcess.getInputStream();

        assert pingProcessor != null;
        pingProcessor.process(inputStream);
        return false;
    }

    private PingProcessor getOsSpecificProcessorStrategy(OS os) {
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
