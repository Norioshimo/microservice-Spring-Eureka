package com.ms.products.controllers;

import com.ms.products.entities.Product;
import com.ms.products.services.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping
@Slf4j
public class ProductoController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<Product> list() {
        return this.productService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> details(@PathVariable Long id) throws InterruptedException {

        if(id.equals(10L)){// Simulación de error...
            throw new IllegalStateException("Producto no encontrado;");
        }

        if(id.equals(7L)){// Simulación de error...
            TimeUnit.SECONDS.sleep(5l);
        }

        Optional<Product> producto = this.productService.findById(id);
        if (producto.isPresent()) {
            return ResponseEntity.ok(producto.orElseThrow());
        }

        return ResponseEntity.notFound().build();
    }

}
