CREATE TABLE devices (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    brand VARCHAR(50) NOT NULL,
    state VARCHAR(20) NOT NULL,
    creation_time TIMESTAMP NOT NULL DEFAULT now(),
    version BIGINT NOT NULL DEFAULT 0
);

CREATE INDEX idx_device_state ON devices (state);

CREATE INDEX idx_device_brand ON devices (brand);