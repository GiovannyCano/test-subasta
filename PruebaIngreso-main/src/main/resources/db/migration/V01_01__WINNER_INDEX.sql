CREATE INDEX IF NOT EXISTS idx_apuesta_item_monto_desc
ON subasta_apuesta (apuesta_item_id, apuesta_monto DESC);
