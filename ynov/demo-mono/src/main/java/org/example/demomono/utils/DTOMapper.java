package org.example.demomono.utils;
import org.example.demomono.dto.OrderDTO;
import org.example.demomono.dto.ProductDTO;
import org.example.demomono.model.Order;
import org.example.demomono.model.Product;

import java.time.LocalDate;

public class DTOMapper {


public static ProductDTO convertToDto(Product product) {
    ProductDTO productDTO = new ProductDTO();
    productDTO.setId(product.getId());
    productDTO.setName(product.getName());
    productDTO.setPrice(product.getPrice());
    return productDTO;
}

public static Product convertToDo(ProductDTO productDTO){
    Product product = new Product();
    product.setId(productDTO.getId());
    product.setName(productDTO.getName());
    product.setPrice(productDTO.getPrice());
    return product;
}


public static OrderDTO convertToDto(Order order) {
    OrderDTO orderDTO = new OrderDTO();
    orderDTO.setId(order.getId());
    orderDTO.setOrderNumber(order.getOrderNumber());
    orderDTO.setFormattedOrderDate(order.getOrderDate().toString());
    orderDTO.setProductId(order.getProduct().getId());
    ProductDTO productDTO = convertToDto(order.getProduct());
    orderDTO.setProductDTO(productDTO);
    return orderDTO;

}


public static Order convertToDo(OrderDTO orderDTO, Product product){
    Order order = new Order();
    order.setId(orderDTO.getId());
    order.setOrderNumber(orderDTO.getOrderNumber());
    order.setOrderDate(LocalDate.parse(orderDTO.getFormattedOrderDate()));
    order.setProduct(product);
    return order;
}

}


