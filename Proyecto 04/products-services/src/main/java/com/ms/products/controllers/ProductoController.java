package com.ms.products.controllers;

import com.ms.products.entities.Product;
import com.ms.products.services.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

        if (id.equals(10L)) {// Simulación de error...
            throw new IllegalStateException("Producto no encontrado;");
        }

        if (id.equals(7L)) {// Simulación de error...
            TimeUnit.SECONDS.sleep(5l);
        }

        Optional<Product> producto = this.productService.findById(id);
        if (producto.isPresent()) {
            return ResponseEntity.ok(producto.orElseThrow());
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Product> create(@RequestBody Product product) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.productService.save(product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> editar(@RequestBody Product product, @PathVariable Long id) {
        Optional<Product> producto = this.productService.findById(id);
        if (producto.isPresent()) {
            Product productDb = producto.orElseThrow();
            productDb.setName(product.getName());
            productDb.setPrice(product.getPrice());
            productDb.setCreateAt(product.getCreateAt());

            return ResponseEntity.status(HttpStatus.CREATED).body(this.productService.save(productDb));
        }


        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<Product> producto = this.productService.findById(id);
        if (producto.isPresent()) {
            this.productService.deleteById(id);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

}
