package com.ms.items.controllers;

import com.ms.items.models.Item;
import com.ms.items.models.Product;
import com.ms.items.services.ItemService;
import com.netflix.discovery.converters.Auto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
@Slf4j
public class ItemController {

    @Autowired
    private ItemService itemService;
    @Autowired
    private CircuitBreakerFactory circuitBreakerFactory;


    @GetMapping()
    public List<Item> list() {
        return this.itemService.findAll();
    }

    /*
     * Circuit breaker usar por código de AppConfig.java
     * **/
    @GetMapping("/{id}")
    public ResponseEntity<?> details(@PathVariable Long id) {

        Optional<Item> item = this.circuitBreakerFactory.create("items")
                .run(() -> this.itemService.findById(id),
                        e -> {
                            log.info("Error: " + e.getMessage());
                            Item it = new Item(
                                    Product.builder()
                                            .createAt(LocalDate.now())
                                            .id(1L)
                                            .name("Camara Sony - Circuit Breaker")
                                            .price(500.0)
                                            .build(),
                                    5);

                            return Optional.of(it);
                        });

        if (item.isPresent()) {
            return ResponseEntity.ok(item.get());
        }


        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", "No existe el producto en el servicio producto"));
    }

    /*
     * Configurar Circuit Breaker usando las configuraciones de YML
     * */
    @GetMapping("/details/{id}")
    @CircuitBreaker(name = "items", fallbackMethod = "fallBackMethodCB")
    @TimeLimiter(name = "items", fallbackMethod = "fallBackMethodTimeOut")
    public CompletableFuture<?> details2(@PathVariable Long id) {
        return CompletableFuture.supplyAsync(() -> {
            Optional<Item> item = this.itemService.findById(id);
            if (item.isPresent()) {
                return ResponseEntity.ok(item.get());
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", "No existe el producto en el servicio producto"));
        });
    }

    public CompletableFuture<?> fallBackMethodCB(Throwable e) {
        log.info("Error: " + e.getMessage());
        return CompletableFuture.supplyAsync(() -> {
            Item it = new Item(
                    Product.builder()
                            .createAt(LocalDate.now())
                            .id(1L)
                            .name("Camara Sony - Circuit Breaker por fallBackMethodCB")
                            .price(500.0)
                            .build(),
                    5);
            return ResponseEntity.ok(it);
        });
    }

    public CompletableFuture<?> fallBackMethodTimeOut(Throwable e) {
        log.info("Error: " + e.getMessage());
        return CompletableFuture.supplyAsync(() -> {
            Item it = new Item(
                    Product.builder()
                            .createAt(LocalDate.now())
                            .id(1L)
                            .name("Camara Sony - Circuit Breaker por fallBackMethodTimeOut")
                            .price(500.0)
                            .build(),
                    5);
            return ResponseEntity.ok(it);
        });
    }

    @GetMapping("/forward-message")
    public ResponseEntity<?>forwardMessage(){
        return ResponseEntity.status(HttpStatus.OK).body("Error y se hizo forward");

    }
}
