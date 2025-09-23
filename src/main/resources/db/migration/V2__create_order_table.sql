CREATE TABLE orders
(
    id           UUID PRIMARY KEY,
    user_id      VARCHAR(255),
    amount DOUBLE,
    status       VARCHAR(50),
--     retry_status JSONB,
    retry_status TEXT,
    created_at   TIMESTAMP,
    updated_at   TIMESTAMP
);
