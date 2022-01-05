package xyz.vladkozlov.pinguu;

public class PingDataFactory {

    private static String stringToIp(String s) {
        return s.substring(0, s.length()-1);
    }

    private static int stringToBytes(String s) {
        return Integer.parseInt(s);
    }

    private static int stringToTimeInMs(String s) {
        return Integer.parseInt(s.substring(0, s.length()-2));
    }

    private static int stringToTTL(String s) {
        return Integer.parseInt(s);
    }

    public PingData createPingData(String ip, String bytes, String time, String ttl) {
        return new PingData(stringToIp(ip), stringToBytes(bytes), stringToTimeInMs(time), stringToTTL(ttl));
    }
}
