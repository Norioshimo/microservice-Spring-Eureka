package com.ms.products.services;

import com.ms.products.entities.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product>findAll();
    Optional<Product>findById(Long id);
}
