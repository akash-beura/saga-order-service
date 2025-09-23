CREATE TABLE item
(
    item_id   VARCHAR(255) PRIMARY KEY,
    item_name VARCHAR(255) NOT NULL,
    item_price DOUBLE
);


INSERT INTO item (item_id, item_name, item_price)
VALUES ('item1', 'Laptop', 1200.0),
       ('item2', 'Mouse', 25.0);