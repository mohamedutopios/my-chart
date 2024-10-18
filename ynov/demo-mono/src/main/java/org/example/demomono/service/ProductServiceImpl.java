package org.example.demomono.service;

import org.example.demomono.dto.ProductDTO;
import org.example.demomono.exception.ResourceNotFoundException;
import org.example.demomono.model.Product;
import org.example.demomono.repository.ProductRepository;
import org.example.demomono.utils.DTOMapper;
import org.springframework.stereotype.Service;

import java.util.List;

//@Service
public class ProductServiceImpl  {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

   // @Override
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(DTOMapper::convertToDto)
                .toList();
    }

   // @Override
    public ProductDTO getProductById(Long id) {
        Product product = productRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
            return DTOMapper.convertToDto(product);
    }

   // @Override
    public ProductDTO addProduct(ProductDTO productDTO) {
        Product product = DTOMapper.convertToDo(productDTO);
       return DTOMapper.convertToDto(productRepository.save(product));
    }

   // @Override
    public ProductDTO updateProduct(ProductDTO productDTO) {
        Product product1 = productRepository.findById(productDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
            product1.setName(productDTO.getName());
            product1.setPrice(productDTO.getPrice());
            return DTOMapper.convertToDto(productRepository.save(product1));
    }

   // @Override
    public void deleteProduct(Long id) {
        Product product = productRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
            productRepository.delete(product);
    }
}
