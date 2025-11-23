package com.ms.items.services;

import com.ms.items.clients.ProductFeignClient;
import com.ms.items.models.Item;
import com.ms.items.models.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ItemServiceFeign implements ItemService {

    @Autowired
    private ProductFeignClient productFeignClient;

    @Override
    public List<Item> findAll() {
        return this.productFeignClient.findAll().stream().map(product -> {
            Random random = new Random();
            return new Item(product, random.nextInt(10) + 1);
        }).collect(Collectors.toList());
    }


    @Override
    public Optional<Item> findById(Long id) {
        try {
            Product product = this.productFeignClient.details(id);
            return Optional.of(new Item(this.productFeignClient.details(id), new Random().nextInt(10) + 1));
        } catch (Exception e) {
            return Optional.empty();
        }

    }
}
