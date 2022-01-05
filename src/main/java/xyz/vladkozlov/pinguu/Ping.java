package xyz.vladkozlov.pinguu;

import xyz.vladkozlov.pinguu.processors.MacPingProcessor;
import xyz.vladkozlov.pinguu.processors.PingProcessor;
import xyz.vladkozlov.pinguu.processors.WindowsPingProcessor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class Ping {
    private static final String[] BLACKLISTED_STRINGS_WINDOWS = {" ", "Pinging "};
    public static final int ANALYTICS_CAPACITY = 10000;
    private static boolean showInputString = false;
    private static boolean showCustomOutputString = false;
    private PingProcess pingProcess;
    private URL host;
    private PingProcessor pingProcessor;

    public Ping(URL host) {
        this.host = host;
        this.pingProcess = new PingProcess(this.host);
    }

    public boolean start() throws IOException {
        pingProcess.start();

        pingProcessor = getOsSpecificProcessorStrategy(pingProcess.getOS());
        var inputStream = pingProcess.getInputStream();

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
