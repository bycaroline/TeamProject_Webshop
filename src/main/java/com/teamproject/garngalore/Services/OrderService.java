package com.teamproject.garngalore.Services;

import com.teamproject.garngalore.Models.Order;
import com.teamproject.garngalore.Repositories.OrderRepository;
import com.mongodb.MongoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public void createOrder(Order order) {
        orderRepository.save(order);
    }

    public Order getOrderById(String id) {
        Optional<Order> optionalOrder = orderRepository.findById(id);

        if (optionalOrder.isPresent()) {
            return optionalOrder.get();
        } else {
            throw new MongoException("Order med ID " + id + " hittades inte.");
        }
    }

    public void deleteOrder(String id) {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
        } else {
            throw new MongoException("Order med ID " + id + " finns inte i databasen.");
        }
    }

    public void updateOrder(String id, Order order) {
        if (orderRepository.existsById(id)) {
            order.setId(id);
            orderRepository.save(order);
        } else {
            throw new MongoException("Order med ID " + id + " finns inte i databasen.");
        }
    }

    public Iterable<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public void saveOrder(Order order) {
        orderRepository.save(order);
    }
}

