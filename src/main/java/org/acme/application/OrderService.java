package org.acme.application;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.acme.domain.Order;
import org.acme.domain.OrderItem;
import org.acme.domain.OrderStatus;
import org.acme.domain.User;
import org.acme.messaging.OrderEventProducer;
import org.acme.messaging.event.OrderCreatedEvent;
import org.acme.repository.OrderRepository;
import org.acme.repository.UserRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@ApplicationScoped
public class OrderService {

    @Inject
    OrderRepository orderRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    OrderEventProducer eventProducer;

    @Transactional
    public Order addOrder(Long userId, List<OrderItem> items) {
        User user = userRepository.findByIdOptional(userId)
            .orElseThrow(() -> new NotFoundException("User not found"));

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.CREATED);
        order.setCreatedAt(Instant.now());
        order.setItems(items);

        BigDecimal total = items.stream()
            .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalAmount(total);
        items.forEach(i -> i.setOrder(order));

        orderRepository.persist(order);
        OrderCreatedEvent event = new OrderCreatedEvent(order.getId(),userId,order.getTotalAmount().doubleValue(),Instant.now());
        eventProducer.publishOrderCreated(event);
        return order;
    }

    public List<Order> getCreatedOrders() {
        return orderRepository.findCreatedOrders();
    }

    public Order getOrder(Long id) {
        return orderRepository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Order not found"));
    }
}