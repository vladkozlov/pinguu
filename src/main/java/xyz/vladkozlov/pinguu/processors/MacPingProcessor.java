package xyz.vladkozlov.pinguu.processors;

import xyz.vladkozlov.pinguu.PingData;
import xyz.vladkozlov.pinguu.PingException;
import xyz.vladkozlov.pinguu.events.Event;
import xyz.vladkozlov.pinguu.events.EventType;
import xyz.vladkozlov.pinguu.utils.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.regex.Pattern;

public class MacPingProcessor extends PingProcessor implements Processor {
    private final String regex = "(\\d+) bytes from " + Utils.IPV4_PATTERN + ": icmp_seq=(\\d+) ttl=(\\d+) time=(\\d+(\\.\\d+)?) ms";
    private final Pattern pattern = Pattern.compile(regex);
    private long eventId;

    @Override
    public void process(BufferedReader inputStream) throws IOException {
        String originalString;
        while ((originalString = inputStream.readLine()) != null)
        {
            var pingData = getPingDataFromString(originalString);
            if (pingData.isPresent()) {
                var event = new Event<>(eventId, LocalDateTime.now(), pingData.get());
                eventId++;
                super.notify(EventType.PING_EVENT, event);
            } else {
                try {
                    throwIfStringIsException(originalString);
                } catch (PingException e) {
                    var event = new Event<>(eventId, LocalDateTime.now(), e);
                    eventId++;
                    super.notify(EventType.EXCEPTION_EVENT, event);
                }
            }
        }
    }

    private void throwIfStringIsException(String originalString) throws PingException {
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
