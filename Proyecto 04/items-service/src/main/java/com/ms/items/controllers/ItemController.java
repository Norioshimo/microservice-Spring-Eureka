package com.ms.items.controllers;

import com.ms.items.models.Item;
import com.ms.items.models.Product;
import com.ms.items.services.ItemService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@RestController
@Slf4j
public class ItemController {

    @Autowired
    private ItemService itemService;
    @Autowired
    private CircuitBreakerFactory circuitBreakerFactory;

    @Value("${configuracion.texto}")
    private String text;

    @Autowired
    private Environment env;


    @GetMapping("/fetch-configs")
    public ResponseEntity<?> fetchConfigs(@Value("${server.port}") String port) {
        Map<String, String> json = new HashMap<>();
        json.put("text", text);
        json.put("port", port);
        log.info("text: " + text);
        log.info("port: " + port);

        if (env.getActiveProfiles().length > 0 && env.getActiveProfiles()[0].equals("dev")) {
            json.put("autor.nombre", env.getProperty("configuracion.autor.nombre"));
            json.put("autor.email", env.getProperty("configuracion.autor.email"));
        }

        return ResponseEntity.ok(json);
    }

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
    public ResponseEntity<?> forwardMessage() {
        return ResponseEntity.status(HttpStatus.OK).body("Error y se hizo forward");

    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product create(@RequestBody Product product) {
        return this.itemService.save(product);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Product update(@RequestBody Product product, @PathVariable Long id) {
        return this.itemService.update(product, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        this.itemService.deleteById(id);
    }


}
