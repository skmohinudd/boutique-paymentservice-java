CREATE TABLE IF NOT EXISTS payment_refunds(
 id UUID PRIMARY KEY, payment_id UUID NOT NULL REFERENCES payments(id),
 amount NUMERIC(19,2) NOT NULL, reason VARCHAR(300) NOT NULL,
 status VARCHAR(20) NOT NULL, created_at TIMESTAMPTZ NOT NULL
);
CREATE INDEX IF NOT EXISTS idx_payment_refunds_payment ON payment_refunds(payment_id);
