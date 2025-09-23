package com.akash.orderservice.mapper;

import com.akash.orderservice.dto.OrderRequest;
import com.akash.orderservice.dto.OrderResponse;
import com.akash.orderservice.model.Item;
import com.akash.orderservice.model.Order;
import com.akash.orderservice.repository.ItemRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class OrderMapper {

    @Autowired
    protected ItemRepository itemRepository;


    public abstract Order toEntity(OrderRequest request);

    public abstract OrderResponse toResponse(Order order);

    public abstract List<OrderResponse> toResponseList(List<Order> orders);

    @Named("mapItemIdsToItems")
    protected List<Item> mapItemIdsToItems(List<String> itemIds) {
        return itemRepository.findAllByItemIdIn(itemIds);
    }


}
