-- Sincroniza la secuencia para evitar colisiones de id_movimiento tras cargas manuales o restores.
SELECT pg_catalog.setval(
    'public.movimiento_stock_id_movimiento_seq',
    COALESCE((SELECT MAX(id_movimiento) + 1 FROM public.movimiento_stock), 1),
    false
);
