--liquibase formatted sql

--changeset genezeiniss:1
CREATE TABLE IF NOT EXISTS transaction (
    id UUID PRIMARY KEY,
    user_id VARCHAR(64) NOT NULL,
    customer_id VARCHAR(64) NOT NULL,
    payment_method VARCHAR(32) NOT NULL,
    price NUMERIC(10, 2) NOT NULL,
    price_modifier NUMERIC(10, 2) NOT NULL,
    final_price NUMERIC(10, 2) NOT NULL,
    points INTEGER NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    record_created_at TIMESTAMP NOT NULL -- control field
);

CREATE INDEX IF NOT EXISTS idx_transaction_user_id_created_at ON transaction(user_id, created_at);

--changeset genezeiniss:2
CREATE TABLE transaction_metadata (
    id UUID PRIMARY KEY,
    transaction_id UUID NOT NULL,
    attribute VARCHAR(32) NOT NULL,
    data BYTEA NOT NULL, -- for encrypted storage -- todo: add encryption
    record_created_at TIMESTAMP NOT NULL, -- control field
    CONSTRAINT fk_transaction_metadata_transaction
      FOREIGN KEY (transaction_id) REFERENCES transaction(id),
    CONSTRAINT uk_transaction_metadata_attribute UNIQUE (transaction_id, attribute)
);
