package org.example.demomono.service;

import org.example.demomono.dto.OrderDTO;
import org.example.demomono.model.Order;

import java.util.List;

public interface OrderService {

    List<OrderDTO> getAllOrders();
    OrderDTO saveOrder(OrderDTO order);
    OrderDTO getOrderById(Long id);
    void deleteOrder(Long id);
    OrderDTO updateOrder(Long id,OrderDTO order);



}
