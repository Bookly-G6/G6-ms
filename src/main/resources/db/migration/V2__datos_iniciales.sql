SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Data for Name: autor_artista; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.autor_artista VALUES (2, 'Robert C. Martin', 'Conocido como Uncle Bob, referente en arquitectura de software.', true);
INSERT INTO public.autor_artista VALUES (4, 'Stephen King', 'Maestro del terror literario.', true);
INSERT INTO public.autor_artista VALUES (5, 'J.K. Rowling', 'Creadora del universo de Harry Potter.', true);
INSERT INTO public.autor_artista VALUES (1, 'Stephen King', 'Maestro del terror y suspenso.', false);
INSERT INTO public.autor_artista VALUES (3, 'Robert C. Martin', 'Ingeniero de software y autor de Clean Code.', false);


--
-- Data for Name: persona; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: cliente; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: carrito; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: editorial_sello; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.editorial_sello VALUES (1, 'Penguin Random House', true);
INSERT INTO public.editorial_sello VALUES (2, 'O''Reilly Media', true);
INSERT INTO public.editorial_sello VALUES (3, 'Planeta', true);
INSERT INTO public.editorial_sello VALUES (4, 'Penguin Random House', true);
INSERT INTO public.editorial_sello VALUES (5, 'O''Reilly Media', true);
INSERT INTO public.editorial_sello VALUES (6, 'Ivrea', true);


--
-- Data for Name: rango_etario; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.rango_etario VALUES (1, 'Adultos');
INSERT INTO public.rango_etario VALUES (2, 'Jóvenes/Adolescentes');
INSERT INTO public.rango_etario VALUES (3, 'Infantil');
INSERT INTO public.rango_etario VALUES (4, 'Infantil (0-12 años)');
INSERT INTO public.rango_etario VALUES (5, 'Juvenil (13-17 años)');
INSERT INTO public.rango_etario VALUES (6, 'Adultos (18+)');


--
-- Data for Name: tipo_producto; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.tipo_producto VALUES (1, 'Libro Físico', true);
INSERT INTO public.tipo_producto VALUES (2, 'E-book', true);
INSERT INTO public.tipo_producto VALUES (3, 'Audiolibro', true);
INSERT INTO public.tipo_producto VALUES (4, 'Manga', true);
INSERT INTO public.tipo_producto VALUES (5, 'Comic', true);
INSERT INTO public.tipo_producto VALUES (6, 'Vinilo', true);


--
-- Data for Name: producto; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.producto VALUES ('11111111-1111-1111-1111-111111111111', '9780132350884', 'Clean Code', 'Referencia esencial de desarrollo', 2500.00, 3500.00, true, 1, 1, 1, '{"edicion":"1"}'::jsonb, 12);

--
-- Data for Name: carrito_item; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: categoria; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.categoria VALUES (1, 'Ficción', true);
INSERT INTO public.categoria VALUES (2, 'Programación', true);
INSERT INTO public.categoria VALUES (3, 'Terror', true);
INSERT INTO public.categoria VALUES (4, 'Programación y Tecnología', true);
INSERT INTO public.categoria VALUES (5, 'Ficción y Fantasía', true);
INSERT INTO public.categoria VALUES (6, 'Terror', true);


--
-- Data for Name: empleado; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: estado_venta; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.estado_venta VALUES (1, 'CONFIRMADA');


--
-- Data for Name: promocion; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.promocion VALUES (1, 'Descuento 5%', 5.00, true);
INSERT INTO public.promocion VALUES (2, 'Descuento 10%', 10.00, true);


--
-- Data for Name: venta; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: detalle_venta; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: envio; Type: TABLE DATA; Schema: public; Owner: postgres
--




--
-- Data for Name: forma_pago; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.forma_pago VALUES (1, 'Efectivo');
INSERT INTO public.forma_pago VALUES (2, 'Transferencia');
INSERT INTO public.forma_pago VALUES (3, 'Tarjeta de crédito');
INSERT INTO public.forma_pago VALUES (4, 'Tarjeta de débito');


--
-- Data for Name: historial_envio; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: historial_precio; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: movimiento_stock; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: producto_autor; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: producto_categoria; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: rol; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.rol VALUES (1, 'CLIENTE');
INSERT INTO public.rol VALUES (2, 'ADMIN');
INSERT INTO public.rol VALUES (3, 'VENDEDOR');


--
-- Data for Name: usuario; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: venta_pago; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Name: autor_artista_id_autor_artista_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.autor_artista_id_autor_artista_seq', 5, true);


--
-- Name: categoria_id_categoria_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.categoria_id_categoria_seq', 6, true);


--
-- Name: detalle_venta_id_detalle_venta_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.detalle_venta_id_detalle_venta_seq', 1, false);


--
-- Name: editorial_sello_id_editorial_sello_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.editorial_sello_id_editorial_sello_seq', 6, true);


--
-- Name: estado_venta_id_estado_venta_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.estado_venta_id_estado_venta_seq', 1, false);


--
-- Name: forma_pago_id_forma_pago_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.forma_pago_id_forma_pago_seq', 4, true);


--
-- Name: historial_envio_id_historial_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.historial_envio_id_historial_seq', 2, true);


--
-- Name: historial_precio_id_historial_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.historial_precio_id_historial_seq', 1, false);


--
-- Name: movimiento_stock_id_movimiento_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.movimiento_stock_id_movimiento_seq', 1, false);


--
-- Name: promocion_id_promocion_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.promocion_id_promocion_seq', 2, true);


--
-- Name: rango_etario_id_rango_etario_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.rango_etario_id_rango_etario_seq', 6, true);


--
-- Name: rol_id_rol_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.rol_id_rol_seq', 1, false);


--
-- Name: tipo_producto_id_tipo_producto_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.tipo_producto_id_tipo_producto_seq', 6, true);


--
-- Name: venta_pago_id_venta_pago_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.venta_pago_id_venta_pago_seq', 1, false);


