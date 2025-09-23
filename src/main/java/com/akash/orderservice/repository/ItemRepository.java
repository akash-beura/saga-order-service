package com.akash.orderservice.repository;

import com.akash.orderservice.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, String> {

    List<Item> findAllByItemIdIn(List<String> itemIds);
}
