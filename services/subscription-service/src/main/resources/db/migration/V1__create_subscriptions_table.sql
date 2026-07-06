CREATE TABLE subscriptions (
    id UUID PRIMARY KEY,
    customer_id UUID NOT NULL,
    device_id UUID NOT NULL,
    plan_code VARCHAR(80) NOT NULL,
    status VARCHAR(32) NOT NULL,
    activated_at TIMESTAMPTZ,
    suspended_at TIMESTAMPTZ,
    cancelled_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,
    version BIGINT NOT NULL
);

CREATE INDEX idx_subscriptions_device_id ON subscriptions (device_id);
CREATE INDEX idx_subscriptions_customer_id ON subscriptions (customer_id);
