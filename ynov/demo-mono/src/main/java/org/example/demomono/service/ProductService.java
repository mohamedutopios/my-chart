package org.example.demomono.service;

import org.example.demomono.dto.ProductDTO;
import org.example.demomono.model.Product;

import java.util.List;

public interface ProductService {

    List<ProductDTO> getAllProducts();
    ProductDTO getProductById(Long id);
    ProductDTO addProduct(ProductDTO productDTO);
    ProductDTO updateProduct(ProductDTO product);
    void deleteProduct(Long id);

}
