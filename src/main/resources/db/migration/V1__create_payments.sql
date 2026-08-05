CREATE TABLE payments (
 id UUID PRIMARY KEY,
 order_id UUID NOT NULL,
 idempotency_key VARCHAR(100) NOT NULL UNIQUE,
 amount NUMERIC(19,2) NOT NULL,
 currency VARCHAR(3) NOT NULL,
 status VARCHAR(20) NOT NULL,
 provider_reference VARCHAR(100) NOT NULL,
 created_at TIMESTAMPTZ NOT NULL
);
CREATE INDEX idx_payments_order_id ON payments(order_id);
