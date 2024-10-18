package org.example.demomono.dto;

import jakarta.persistence.OneToOne;
import lombok.Data;
import org.example.demomono.model.Product;

import java.time.LocalDate;

@Data
public class OrderDTO {

    private Long id;
    private String orderNumber;
    private String formattedOrderDate;
    private Long productId;
    private ProductDTO productDTO;

}
