package xyz.vladkozlov.pinguu.processors;

import xyz.vladkozlov.pinguu.Ping;
import xyz.vladkozlov.pinguu.PingData;
import xyz.vladkozlov.pinguu.PingException;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Optional;
import java.util.regex.Pattern;

public class MacPingProcessor implements PingProcessor {
    private final String IPV4_PATTERN ="(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})";
    private final String regex = "(\\d+) bytes from " + IPV4_PATTERN + ": icmp_seq=(\\d+) ttl=(\\d+) time=(\\d+(\\.\\d+)?) ms";
    private final Pattern pattern = Pattern.compile(regex);

    @Override
    public void process(BufferedReader inputStream) throws IOException {
        String originalString;
        while ((originalString = inputStream.readLine()) != null)
        {
            System.out.println(originalString);

            var pingData = getPingDataFromString(originalString);
            if (pingData.isPresent()) {
                System.out.printf("Latency %s\n", pingData.get().time());
            } else {
                try {
                    throwIfStringIsError(originalString);
                } catch (PingException e) {
                    System.err.println(e.getMessage());
                }
            }
        }
    }

    private void throwIfStringIsError(String originalString) throws PingException {
        if (originalString.startsWith("ping: sendto: No route to host")) {
            throw new PingException("No route to host");
        } else if (originalString.startsWith("Request timeout for")) {
            throw new PingException("Request timeout");
        } else if (originalString.startsWith("ping: cannot resolve")) {
            throw new PingException("Cannot resolve host");
        }
    }

    private Optional<PingData> getPingDataFromString(String originalString) {
        var matcher = pattern.matcher(originalString);

        if (!matcher.find()) return Optional.empty();

        var bytesString = matcher.group(1);
        var ipString = matcher.group(2);
        var ttlString = matcher.group(4);
        var timeString = matcher.group(5);

        return Optional.of(new PingData(ipString, Integer.parseInt(bytesString), (int) Math.round(Double.parseDouble(timeString)), Integer.parseInt(ttlString)));
    }

}
