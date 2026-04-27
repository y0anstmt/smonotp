package org.acme.messaging;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;


import jakarta.enterprise.context.ApplicationScoped;
import org.acme.messaging.event.OrderCreatedEvent;

@ApplicationScoped
public class OrderEventProducer {
    @Channel("order-created")
    Emitter<OrderCreatedEvent> emitter;

    public void publishOrderCreated(OrderCreatedEvent event) {
        emitter.send(event);
    }
}
