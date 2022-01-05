package xyz.vladkozlov.pinguu.processors;

import java.io.BufferedReader;
import java.io.IOException;

public interface PingProcessor {
    void process(BufferedReader inputStream) throws IOException;
}
