CREATE TABLE esim_profiles (
    iccid VARCHAR(32) PRIMARY KEY,
    customer_id UUID NOT NULL,
    device_id UUID NOT NULL,
    subscription_id UUID,
    status VARCHAR(32) NOT NULL,
    activation_code VARCHAR(255) NOT NULL,
    smdp_address VARCHAR(255) NOT NULL,
    provisioned_at TIMESTAMPTZ NOT NULL,
    activated_at TIMESTAMPTZ,
    suspended_at TIMESTAMPTZ,
    terminated_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,
    version BIGINT NOT NULL
);

CREATE INDEX idx_esim_profiles_customer_id ON esim_profiles (customer_id);
CREATE INDEX idx_esim_profiles_device_id ON esim_profiles (device_id);
CREATE INDEX idx_esim_profiles_subscription_id ON esim_profiles (subscription_id);
