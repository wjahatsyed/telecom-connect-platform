CREATE TABLE devices (
    id UUID PRIMARY KEY,
    customer_id UUID NOT NULL,
    name VARCHAR(120) NOT NULL,
    imei VARCHAR(16),
    eid VARCHAR(64),
    type VARCHAR(32) NOT NULL,
    status VARCHAR(32) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,
    version BIGINT NOT NULL
);

ALTER TABLE devices
    ADD CONSTRAINT uk_devices_imei UNIQUE (imei);

ALTER TABLE devices
    ADD CONSTRAINT uk_devices_eid UNIQUE (eid);
