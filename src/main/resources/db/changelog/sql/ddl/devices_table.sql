CREATE TABLE devices (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    brand VARCHAR(50) NOT NULL,
    state VARCHAR(20) NOT NULL,
    creation_time TIMESTAMP NOT NULL
);

CREATE INDEX idx_device_name ON devices (name);

CREATE INDEX idx_device_state ON devices (state);