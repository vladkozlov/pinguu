package xyz.vladkozlov.pinguu;

public record PingData(
        String ip,
        int bytes,
        int time,
        int ttl) {}
