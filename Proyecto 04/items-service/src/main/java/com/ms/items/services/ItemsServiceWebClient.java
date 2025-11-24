package com.ms.items.services;

import com.ms.items.models.Item;
import com.ms.items.models.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@Primary
@Service
@Slf4j
public class ItemsServiceWebClient implements ItemService {

    @Autowired
    private WebClient.Builder webClient;

    @Override
    public List<Item> findAll() {
        log.info("Consumido por ItemsServiceWebClient");
        return webClient.build().get()
                .retrieve()
                .bodyToFlux(Product.class)
                .map(product -> {
                    Random random = new Random();
                    return new Item(product, random.nextInt(10) + 1);
                })
                .collectList()
                .block();
    }

    @Override
    public Optional<Item> findById(Long id) {
        Map<String, Long> params = new HashMap<>();
        params.put("id", id);
        //try{
        return Optional.ofNullable(webClient.build().get().uri("/{id}", params)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Product.class)
                .map(product -> {
                    Random random = new Random();
                    return new Item(product, random.nextInt(10) + 1);
                })
                .block());
        //} catch (Exception e) {
        //   return Optional.empty();
        //}
    }
}
