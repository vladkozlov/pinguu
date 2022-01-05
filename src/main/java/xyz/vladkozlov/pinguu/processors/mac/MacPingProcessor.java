package xyz.vladkozlov.pinguu.processors.mac;

import xyz.vladkozlov.pinguu.PingData;
import xyz.vladkozlov.pinguu.processors.PingProcessor;
import xyz.vladkozlov.pinguu.utils.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Pattern;

public class MacPingProcessor extends PingProcessor {
    private final String regex = "(\\d+) bytes from " + Utils.IPV4_PATTERN + ": icmp_seq=(\\d+) ttl=(\\d+) time=(\\d+(\\.\\d+)?) ms";
    private final Pattern pattern = Pattern.compile(regex);

    public Map<Function<String, Boolean>, String> rules() {
        var rules = new HashMap<Function<String, Boolean>, String>();
        rules.put((s) -> s.startsWith("ping: sendto: No route to host"), "No route to host.");
        rules.put((s) -> s.startsWith("Request timeout for"), "Request timeout");
        rules.put((s) -> s.startsWith("ping: cannot resolve"), "Cannot resolve host");
        return rules;
    }

    public Optional<PingData> parsePingStringToPingData(String pingString) {
        var matcher = pattern.matcher(pingString);

        if (!matcher.find()) return Optional.empty();

        var bytesString = matcher.group(1);
        var ipString = matcher.group(2);
        var ttlString = matcher.group(4);
        var timeString = matcher.group(5);

        return Optional.of(new PingData(ipString, Integer.parseInt(bytesString), (int) Math.round(Double.parseDouble(timeString)), Integer.parseInt(ttlString)));
    }

}
