package org.acme.repository;


import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.domain.Order;
import org.acme.domain.OrderStatus;

import java.util.List;

@ApplicationScoped
public class OrderRepository implements PanacheRepository<Order> {
    public List<Order> findCreatedOrders() {
        return find("status", OrderStatus.CREATED).list();
    }
}
