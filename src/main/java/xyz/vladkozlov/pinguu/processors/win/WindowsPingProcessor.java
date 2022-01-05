package xyz.vladkozlov.pinguu.processors.win;

import xyz.vladkozlov.pinguu.PingData;
import xyz.vladkozlov.pinguu.processors.PingProcessor;
import xyz.vladkozlov.pinguu.utils.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Pattern;

public class WindowsPingProcessor extends PingProcessor {
    private final String regex = "Reply from "+ Utils.IPV4_PATTERN + ": bytes=(\\d+) time=(\\d+)ms TTL=(\\d+)";
    private final Pattern pattern = Pattern.compile(regex);

    public Optional<PingData> parsePingStringToPingData(String pingString) {
        var matcher = pattern.matcher(pingString);

        if (!matcher.find()) return Optional.empty();

        var bytesString = matcher.group(2);
        var ipString = matcher.group(1);
        var ttlString = matcher.group(4);
        var timeString = matcher.group(3);

        return Optional.of(new PingData(ipString, Integer.parseInt(bytesString), (int) Math.round(Double.parseDouble(timeString)), Integer.parseInt(ttlString)));
    }

    public Map<Function<String, Boolean>, String> rules() {
        var rules = new HashMap<Function<String, Boolean>, String>();
        rules.put((s) -> s.endsWith("Destination host unreachable."), "Destination host unreachable.");
        rules.put((s) -> s.startsWith("General failure"), "General failure.");
        rules.put((s) -> s.startsWith("Request timed out"), "Request timed out.");
        rules.put((s) -> s.startsWith("Ping request could not find host"), "Ping request could not find host.");
        return rules;
    }
}
