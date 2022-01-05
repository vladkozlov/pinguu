package xyz.vladkozlov.pinguu.processors;

import java.io.BufferedReader;

public class WindowsPingProcessor implements PingProcessor {

    @Override
    public void process(BufferedReader inputStream) {

    }
/*

    private PingData processPingString(String inputString) {
        if (inputString.startsWith("Reply")) {
            if (inputString.endsWith("Destination host unreachable.")) return new PingData("Destination host unreachable.");

            var arrayOfStrings =inputString.split(" ");
            var ip = arrayOfStrings[2];
            var bytes = parseDataWithEquals(arrayOfStrings[3]);
            var timeInMs = parseDataWithEquals(arrayOfStrings[4]);
            var ttl = parseDataWithEquals(arrayOfStrings[5]);

            return new PingData(ip, bytes, timeInMs, ttl);
        } else if (inputString.startsWith("General failure")) {
            return  new PingData("General failure.");
        } else if (inputString.startsWith("Request timed out")) {
            return new PingData("Request timed out.");
        } else if (inputString.startsWith("xyz.vladkozlov.pinguu.Ping request could not find host")) {
            return  new PingData("xyz.vladkozlov.pinguu.Ping request could not find the host.");
        }

        return null;
    }
*/
}
