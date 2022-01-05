package xyz.vladkozlov.pinguu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class PingProcess {

    private Process pingProcess;
    private final URL host;
    private BufferedReaders bufferedReaders;
    private final OS os;

    public PingProcess(URL host) {
        this.host = host;

        this.os = osStringToOsEnum(System.getProperty("os.name"));
    }

    public Process start() throws IOException {
        if (pingProcess != null) {
            return pingProcess;
        }

        var builder= buildPingProcess(this.host);
        pingProcess = builder.start();
        this.bufferedReaders = getBufferedReadersFromProcess(pingProcess);

        return pingProcess;
    }

    public boolean stop() {
        pingProcess.destroy();
        return true;
    }

    public BufferedReader getInputStream() {
        return this.bufferedReaders.stdInput();
    }

    public BufferedReader getErrorStream() {
        return this.bufferedReaders.stdError();
    }

    private String[] getPingCommand(String host) {
        if (os.equals(OS.MAC)) {
            return new String[]{"ping", host};
        } if (os.equals(OS.WIN)) {
            return new String[]{"ping", host, "-t"};
        }
        return new String[]{};
    }

    private ProcessBuilder buildPingProcess(URL host) {
        var hostString = host.getHost();
        var command = getPingCommand(hostString);
        return new ProcessBuilder(command);
    }

    private BufferedReaders getBufferedReadersFromProcess(Process process) {
        var stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
        var stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

        return new BufferedReaders(stdInput, stdError);
    }

    record BufferedReaders(BufferedReader stdInput, BufferedReader stdError) {}

    private OS osStringToOsEnum(String osString) {
        if (osString.startsWith("OSX") || osString.startsWith("Mac")) {
            return OS.MAC;
        } if (osString.startsWith("Windows")) {
            return OS.WIN;
        }
        return  OS.LINUX;
    }

    public OS getOS() {
        return this.os;
    }
}
