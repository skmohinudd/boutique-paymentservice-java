CREATE TABLE IF NOT EXISTS payment_outbox_events(
  id UUID PRIMARY KEY,
  aggregate_id UUID NOT NULL,
  event_type VARCHAR(100) NOT NULL,
  payload TEXT NOT NULL,
  created_at TIMESTAMPTZ NOT NULL,
  kafka_published_at TIMESTAMPTZ,
  rabbit_published_at TIMESTAMPTZ
);
CREATE INDEX IF NOT EXISTS idx_payment_outbox_pending
ON payment_outbox_events(created_at)
WHERE kafka_published_at IS NULL OR rabbit_published_at IS NULL;
