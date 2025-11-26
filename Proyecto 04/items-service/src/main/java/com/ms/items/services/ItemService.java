package com.ms.items.services;

import com.ms.items.models.Item;
import com.ms.items.models.Product;

import java.util.List;
import java.util.Optional;

public interface ItemService {

    List<Item>findAll();

    Optional<Item>findById(Long id);

    Product save(Product product);

    Product update(Product product,Long id);

    void deleteById(Long id);
}
