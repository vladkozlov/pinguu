package xyz.vladkozlov.pinguu.processors.mac;

import xyz.vladkozlov.pinguu.PingData;
import xyz.vladkozlov.pinguu.PingException;
import xyz.vladkozlov.pinguu.events.Event;
import xyz.vladkozlov.pinguu.events.EventType;
import xyz.vladkozlov.pinguu.processors.PingProcessor;
import xyz.vladkozlov.pinguu.processors.Processor;
import xyz.vladkozlov.pinguu.utils.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
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
            var finalOriginalString = originalString;

            getPingDataFromString(finalOriginalString)
                    .ifPresentOrElse(pd -> super.notify(EventType.PING_EVENT, generateEvent(pd)),
                            () -> rules()
                                    .entrySet()
                                    .stream()
                                    .filter(entry -> entry.getKey().apply(finalOriginalString))
                                    .findFirst()
                                    .ifPresent(entry -> {
                                        var event = generateEvent(new PingException(entry.getValue()));
                                        super.notify(EventType.EXCEPTION_EVENT, event);
                                    }));
        }
    }

    private <T> Event<T> generateEvent(T data) {
        return new Event<>(++eventId, LocalDateTime.now(), data);
    }

    private Map<Function<String, Boolean>, String> rules() {
        var rules = new HashMap<Function<String, Boolean>, String>();
        rules.put((s) -> s.startsWith("ping: sendto: No route to host"), "No route to host.");
        rules.put((s) -> s.startsWith("Request timeout for"), "Request timeout");
        rules.put((s) -> s.startsWith("ping: cannot resolve"), "Cannot resolve host");
        return rules;
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
