create table orders_items(
    order_id UUID not null,
    item_id VARCHAR(255) not null ,
    PRIMARY KEY (order_id, item_id),
    FOREIGN KEY (order_id) references orders(id),
    FOREIGN KEY (item_id) references item(item_id)
)