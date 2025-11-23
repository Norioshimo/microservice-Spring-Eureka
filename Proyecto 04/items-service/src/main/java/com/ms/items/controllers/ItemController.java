package com.ms.items.controllers;

import com.ms.items.models.Item;
import com.ms.items.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
public class ItemController {

    @Autowired
    private ItemService itemService;


    @GetMapping()
    public List<Item> list() {
        return this.itemService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> details(@PathVariable Long id) {
        Optional<Item> item = this.itemService.findById(id);

        if (item.isPresent()) {
            return ResponseEntity.ok(item.get());
        }


        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message","No existe el producto en el servicio producto"));
    }
}
