package com.akash.orderservice.service;

import com.akash.orderservice.model.Item;
import com.akash.orderservice.repository.ItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    public List<Item> findAllItemsByIds(List<String> itemIds) {
        return itemRepository.findAllByItemIdIn(itemIds);
    }

    public List<Item> fetchAllItems() {
        return itemRepository.findAll();
    }

}
