package com.akash.orderservice.controller;

import com.akash.orderservice.model.Item;
import com.akash.orderservice.service.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/item")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public ResponseEntity<List<Item>> fetchAllItems() {
        return ResponseEntity.ok()
                .body(itemService.fetchAllItems());
    }
}
