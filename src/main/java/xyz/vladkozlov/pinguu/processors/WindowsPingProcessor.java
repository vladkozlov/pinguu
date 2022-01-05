package xyz.vladkozlov.pinguu.processors;

import xyz.vladkozlov.pinguu.PingData;
import xyz.vladkozlov.pinguu.PingException;
import xyz.vladkozlov.pinguu.utils.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Optional;
import java.util.regex.Pattern;

public class WindowsPingProcessor implements PingProcessor {
    private final String regex = "Reply from "+ Utils.IPV4_PATTERN + ": bytes=(\\d+) time=(\\d+)ms TTL=(\\d+)";
    private final Pattern pattern = Pattern.compile(regex);

    @Override
    public void process(BufferedReader inputStream) throws IOException {
        String originalString;
        while ((originalString = inputStream.readLine()) != null)
        {
            System.out.println(originalString);

            var pingData = getPingDataFromString(originalString);
            if (pingData.isPresent()) {
                System.out.println(pingData.get());
            } else {
                try {
                    throwIfStringIsError(originalString);
                } catch (PingException e) {
                    System.err.println(e.getMessage());
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


    private void throwIfStringIsError(String originalString) throws PingException {
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
