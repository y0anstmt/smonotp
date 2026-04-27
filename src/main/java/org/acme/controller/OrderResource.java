package org.acme.controller;


import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.application.OrderService;
import org.acme.domain.Order;
import org.acme.domain.OrderItem;
import org.acme.dto.CreateOrderRequest;
import org.acme.dto.OrderItemRequest;

import java.util.List;

@Path("/api/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {
    @Inject
    OrderService orderService;

    @GET
    // @PermitAll
    public Response getAllCreatedOrders() {
        try {
            List<Order> orders = orderService.getCreatedOrders();
            return Response.ok(orders).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error fetching orders: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    // @PermitAll
    public Response getOrder(@PathParam("id") Long id) {
        try {
            Order order = orderService.getOrder(id);
            return Response.ok(order).build();
        } catch (jakarta.ws.rs.NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Order with ID " + id + " not found")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error fetching order: " + e.getMessage())
                    .build();
        }
    }

    @POST
    public Response createOrder(@Valid CreateOrderRequest request) {
        try {
            List<OrderItem> items = request.items().stream()
                    .map(this::mapToOrderItem)
                    .toList();

            Order created = orderService.addOrder(request.userId(), items);
            return Response.status(Response.Status.CREATED).entity(created).build();
        } catch (jakarta.ws.rs.NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error creating order: " + e.getMessage())
                    .build();
        }
    }

    private OrderItem mapToOrderItem(OrderItemRequest itemRequest) {
        OrderItem item = new OrderItem();
        item.setProductName(itemRequest.productName());
        item.setQuantity(itemRequest.quantity());
        item.setPrice(itemRequest.price());
        return item;
    }
}
