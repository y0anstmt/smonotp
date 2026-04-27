package org.acme.messaging.event;

import java.time.Instant;

public record OrderCreatedEvent(
    Long orderId,
    Long userId,
    Double totalAmount,
    Instant timestamp
) { }
