CREATE TABLE customers (
    id UUID PRIMARY KEY,
    name VARCHAR(160) NOT NULL,
    type VARCHAR(32) NOT NULL,
    email VARCHAR(254) NOT NULL,
    phone_number VARCHAR(40),
    status VARCHAR(32) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,
    version BIGINT NOT NULL
);

ALTER TABLE customers
    ADD CONSTRAINT uk_customers_email UNIQUE (email);
