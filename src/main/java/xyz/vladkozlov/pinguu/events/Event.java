package xyz.vladkozlov.pinguu.events;

import java.time.LocalDateTime;

public record Event<T> (
        long id,
        LocalDateTime timestamp,
        T data
){}
