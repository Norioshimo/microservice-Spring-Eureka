package com.ms.products.services;

import com.ms.products.entities.Product;
import com.ms.products.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private Environment environment;


    @Override
    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return ((List<Product>) this.productRepository.findAll()).stream().map(product -> {
            product.setPort(Integer.parseInt(environment.getProperty("local.server.port")));// Buscar en que puerto está ejecutando el microservicio. Prueba para el balanceo de carga
            return product;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> findById(Long id) {
        return this.productRepository.findById(id).map(product -> {
            product.setPort(Integer.parseInt(environment.getProperty("local.server.port")));// Buscar en que puerto está ejecutando el microservicio. Prueba para el balanceo de carga
            return product;
        });
    }
}
