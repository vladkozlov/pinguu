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

public class WindowsPingProcessor extends PingProcessor implements Processor {
    private final String regex = "Reply from "+ Utils.IPV4_PATTERN + ": bytes=(\\d+) time=(\\d+)ms TTL=(\\d+)";
    private final Pattern pattern = Pattern.compile(regex);
    private long eventId=1;

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

    private Optional<PingData> getPingDataFromString(String originalString) {
        var matcher = pattern.matcher(originalString);

        if (!matcher.find()) return Optional.empty();

        var bytesString = matcher.group(2);
        var ipString = matcher.group(1);
        var ttlString = matcher.group(4);
        var timeString = matcher.group(3);

        return Optional.of(new PingData(ipString, Integer.parseInt(bytesString), (int) Math.round(Double.parseDouble(timeString)), Integer.parseInt(ttlString)));
    }


    private void throwIfStringIsException(String originalString) throws PingException {
        if (originalString.endsWith("Destination host unreachable.")) {
            throw new PingException("Destination host unreachable.");
        } else if (originalString.startsWith("General failure")) {
            throw new PingException("General failure");
        } else if (originalString.startsWith("Request timed out")) {
            throw new PingException("Request timed out");
        } else if (originalString.startsWith("Ping request could not find host")) {
            throw new PingException("Ping request could not find host.");
        }
    }

}
