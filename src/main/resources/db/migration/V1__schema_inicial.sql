
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

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: autor_artista; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.autor_artista (
    id_autor_artista integer NOT NULL,
    nombre character varying(255) NOT NULL,
    biografia text,
    activa boolean DEFAULT true
);


ALTER TABLE public.autor_artista OWNER TO postgres;

--
-- Name: autor_artista_id_autor_artista_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.autor_artista_id_autor_artista_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.autor_artista_id_autor_artista_seq OWNER TO postgres;

--
-- Name: autor_artista_id_autor_artista_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.autor_artista_id_autor_artista_seq OWNED BY public.autor_artista.id_autor_artista;


--
-- Name: carrito; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.carrito (
    id_carrito uuid NOT NULL,
    id_cliente uuid NOT NULL,
    activo boolean DEFAULT true NOT NULL,
    fecha_creacion timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    fecha_actualizacion timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE public.carrito OWNER TO postgres;

--
-- Name: carrito_item; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.carrito_item (
    id_item uuid NOT NULL,
    id_carrito uuid NOT NULL,
    id_producto uuid NOT NULL,
    cantidad integer NOT NULL,
    precio_unitario numeric(10,2) NOT NULL,
    CONSTRAINT carrito_item_cantidad_check CHECK ((cantidad > 0)),
    CONSTRAINT carrito_item_precio_unitario_check CHECK ((precio_unitario >= (0)::numeric))
);


ALTER TABLE public.carrito_item OWNER TO postgres;

--
-- Name: categoria; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.categoria (
    id_categoria integer NOT NULL,
    nombre_categoria character varying(255) NOT NULL,
    activa boolean DEFAULT true
);


ALTER TABLE public.categoria OWNER TO postgres;

--
-- Name: categoria_id_categoria_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.categoria_id_categoria_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.categoria_id_categoria_seq OWNER TO postgres;

--
-- Name: categoria_id_categoria_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.categoria_id_categoria_seq OWNED BY public.categoria.id_categoria;


--
-- Name: cliente; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.cliente (
    id_cliente uuid NOT NULL,
    id_persona uuid NOT NULL,
    puntos_fidelidad integer DEFAULT 0
);


ALTER TABLE public.cliente OWNER TO postgres;

--
-- Name: detalle_venta; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.detalle_venta (
    id_detalle_venta integer NOT NULL,
    id_venta uuid NOT NULL,
    id_producto uuid NOT NULL,
    cantidad integer NOT NULL,
    precio_unitario numeric(10,2) NOT NULL,
    id_promocion integer,
    subtotal_renglon numeric(10,2) NOT NULL
);


ALTER TABLE public.detalle_venta OWNER TO postgres;

--
-- Name: detalle_venta_id_detalle_venta_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.detalle_venta_id_detalle_venta_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.detalle_venta_id_detalle_venta_seq OWNER TO postgres;

--
-- Name: detalle_venta_id_detalle_venta_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.detalle_venta_id_detalle_venta_seq OWNED BY public.detalle_venta.id_detalle_venta;


--
-- Name: editorial_sello; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.editorial_sello (
    id_editorial_sello integer NOT NULL,
    nombre character varying(255) NOT NULL,
    activa boolean DEFAULT true
);


ALTER TABLE public.editorial_sello OWNER TO postgres;

--
-- Name: editorial_sello_id_editorial_sello_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.editorial_sello_id_editorial_sello_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.editorial_sello_id_editorial_sello_seq OWNER TO postgres;

--
-- Name: editorial_sello_id_editorial_sello_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.editorial_sello_id_editorial_sello_seq OWNED BY public.editorial_sello.id_editorial_sello;


--
-- Name: empleado; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.empleado (
    id_empleado uuid NOT NULL,
    id_persona uuid NOT NULL,
    legajo character varying(100),
    cargo character varying(100),
    id_sucursal integer NOT NULL
);


ALTER TABLE public.empleado OWNER TO postgres;

--
-- Name: envio; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.envio (
    id_envio uuid NOT NULL,
    id_venta uuid NOT NULL,
    tipo_envio character varying(50) NOT NULL,
    estado_logistica character varying(50) NOT NULL,
    empresa_correo character varying(100),
    numero_tracking character varying(100),
    codigo_retiro character varying(50),
    observaciones text,
    fecha_actualizacion timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    activo boolean DEFAULT true
);


ALTER TABLE public.envio OWNER TO postgres;

--
-- Name: estado_venta; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.estado_venta (
    id_estado_venta integer NOT NULL,
    nombre_estado character varying(255) NOT NULL
);


ALTER TABLE public.estado_venta OWNER TO postgres;

--
-- Name: estado_venta_id_estado_venta_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.estado_venta_id_estado_venta_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.estado_venta_id_estado_venta_seq OWNER TO postgres;

--
-- Name: estado_venta_id_estado_venta_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.estado_venta_id_estado_venta_seq OWNED BY public.estado_venta.id_estado_venta;


--
-- Name: forma_pago; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.forma_pago (
    id_forma_pago integer NOT NULL,
    nombre_pago character varying(255) NOT NULL
);


ALTER TABLE public.forma_pago OWNER TO postgres;

--
-- Name: forma_pago_id_forma_pago_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.forma_pago_id_forma_pago_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.forma_pago_id_forma_pago_seq OWNER TO postgres;

--
-- Name: forma_pago_id_forma_pago_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.forma_pago_id_forma_pago_seq OWNED BY public.forma_pago.id_forma_pago;


--
-- Name: historial_envio; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.historial_envio (
    id_historial integer NOT NULL,
    id_envio uuid NOT NULL,
    estado_anterior character varying(50),
    estado_nuevo character varying(50) NOT NULL,
    fecha_cambio timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    observaciones text,
    id_empleado uuid
);


ALTER TABLE public.historial_envio OWNER TO postgres;

--
-- Name: historial_envio_id_historial_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.historial_envio_id_historial_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.historial_envio_id_historial_seq OWNER TO postgres;

--
-- Name: historial_envio_id_historial_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.historial_envio_id_historial_seq OWNED BY public.historial_envio.id_historial;


--
-- Name: historial_precio; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.historial_precio (
    id_historial integer NOT NULL,
    id_producto uuid NOT NULL,
    precio_costo_anterior numeric(10,2),
    precio_costo_nuevo numeric(10,2),
    precio_venta_anterior numeric(10,2) NOT NULL,
    precio_venta_nuevo numeric(10,2) NOT NULL,
    fecha_cambio timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    id_empleado uuid NOT NULL
);


ALTER TABLE public.historial_precio OWNER TO postgres;

--
-- Name: historial_precio_id_historial_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.historial_precio_id_historial_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.historial_precio_id_historial_seq OWNER TO postgres;

--
-- Name: historial_precio_id_historial_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.historial_precio_id_historial_seq OWNED BY public.historial_precio.id_historial;


--
-- Name: inventario; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.inventario (
    id_sucursal integer NOT NULL,
    id_producto uuid NOT NULL,
    stock integer DEFAULT 0 NOT NULL
);


ALTER TABLE public.inventario OWNER TO postgres;

--
-- Name: movimiento_stock; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.movimiento_stock (
    id_movimiento integer NOT NULL,
    id_sucursal integer NOT NULL,
    id_producto uuid NOT NULL,
    cantidad integer NOT NULL,
    tipo_movimiento character varying(100) NOT NULL,
    fecha timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    id_empleado uuid NOT NULL
);


ALTER TABLE public.movimiento_stock OWNER TO postgres;

--
-- Name: movimiento_stock_id_movimiento_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.movimiento_stock_id_movimiento_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.movimiento_stock_id_movimiento_seq OWNER TO postgres;

--
-- Name: movimiento_stock_id_movimiento_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.movimiento_stock_id_movimiento_seq OWNED BY public.movimiento_stock.id_movimiento;


--
-- Name: persona; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.persona (
    id_persona uuid NOT NULL,
    nombre character varying(255) NOT NULL,
    apellido character varying(255) NOT NULL,
    dni character varying(50),
    telefono character varying(50)
);


ALTER TABLE public.persona OWNER TO postgres;

--
-- Name: producto; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.producto (
    id_producto uuid NOT NULL,
    codigo_barras character varying(50),
    nombre_producto character varying(255) NOT NULL,
    descripcion text,
    precio_costo numeric(10,2) NOT NULL,
    precio_actual numeric(10,2) NOT NULL,
    activo boolean DEFAULT true,
    id_tipo_producto integer NOT NULL,
    id_editorial_sello integer NOT NULL,
    id_rango_etario integer NOT NULL,
    atributos_especificos jsonb
);


ALTER TABLE public.producto OWNER TO postgres;

--
-- Name: producto_autor; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.producto_autor (
    id_producto uuid NOT NULL,
    id_autor_artista integer NOT NULL
);


ALTER TABLE public.producto_autor OWNER TO postgres;

--
-- Name: producto_categoria; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.producto_categoria (
    id_producto uuid NOT NULL,
    id_categoria integer NOT NULL
);


ALTER TABLE public.producto_categoria OWNER TO postgres;

--
-- Name: promocion; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.promocion (
    id_promocion integer NOT NULL,
    nombre_promo character varying(255) NOT NULL,
    porcentaje_descuento numeric(5,2),
    activa boolean DEFAULT true
);


ALTER TABLE public.promocion OWNER TO postgres;

--
-- Name: promocion_id_promocion_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.promocion_id_promocion_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.promocion_id_promocion_seq OWNER TO postgres;

--
-- Name: promocion_id_promocion_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.promocion_id_promocion_seq OWNED BY public.promocion.id_promocion;


--
-- Name: rango_etario; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.rango_etario (
    id_rango_etario integer NOT NULL,
    descripcion character varying(255) NOT NULL
);


ALTER TABLE public.rango_etario OWNER TO postgres;

--
-- Name: rango_etario_id_rango_etario_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.rango_etario_id_rango_etario_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.rango_etario_id_rango_etario_seq OWNER TO postgres;

--
-- Name: rango_etario_id_rango_etario_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.rango_etario_id_rango_etario_seq OWNED BY public.rango_etario.id_rango_etario;


--
-- Name: rol; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.rol (
    id_rol integer NOT NULL,
    nombre_rol character varying(255) NOT NULL
);


ALTER TABLE public.rol OWNER TO postgres;

--
-- Name: rol_id_rol_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.rol_id_rol_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.rol_id_rol_seq OWNER TO postgres;

--
-- Name: rol_id_rol_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.rol_id_rol_seq OWNED BY public.rol.id_rol;


--
-- Name: sucursal; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sucursal (
    id_sucursal integer NOT NULL,
    nombre character varying(255) NOT NULL,
    direccion character varying(255),
    activa boolean DEFAULT true
);


ALTER TABLE public.sucursal OWNER TO postgres;

--
-- Name: sucursal_id_sucursal_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.sucursal_id_sucursal_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.sucursal_id_sucursal_seq OWNER TO postgres;

--
-- Name: sucursal_id_sucursal_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.sucursal_id_sucursal_seq OWNED BY public.sucursal.id_sucursal;


--
-- Name: tipo_producto; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tipo_producto (
    id_tipo_producto integer NOT NULL,
    nombre_tipo character varying(255) NOT NULL,
    activa boolean DEFAULT true
);


ALTER TABLE public.tipo_producto OWNER TO postgres;

--
-- Name: tipo_producto_id_tipo_producto_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.tipo_producto_id_tipo_producto_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.tipo_producto_id_tipo_producto_seq OWNER TO postgres;

--
-- Name: tipo_producto_id_tipo_producto_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.tipo_producto_id_tipo_producto_seq OWNED BY public.tipo_producto.id_tipo_producto;


--
-- Name: usuario; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.usuario (
    id_usuario uuid NOT NULL,
    id_persona uuid NOT NULL,
    email character varying(255) NOT NULL,
    password character varying(255) NOT NULL,
    id_rol integer NOT NULL,
    activo boolean DEFAULT true
);


ALTER TABLE public.usuario OWNER TO postgres;

--
-- Name: venta; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.venta (
    id_venta uuid NOT NULL,
    fecha timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    subtotal_sin_descuentos numeric(10,2) NOT NULL,
    total_final numeric(10,2) NOT NULL,
    origen_venta character varying(50) NOT NULL,
    id_estado_venta integer NOT NULL,
    id_sucursal integer NOT NULL,
    id_cliente uuid,
    id_empleado uuid
);


ALTER TABLE public.venta OWNER TO postgres;

--
-- Name: venta_pago; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.venta_pago (
    id_venta_pago integer NOT NULL,
    id_venta uuid NOT NULL,
    id_forma_pago integer NOT NULL,
    monto_abonado numeric(10,2) NOT NULL
);


ALTER TABLE public.venta_pago OWNER TO postgres;

--
-- Name: venta_pago_id_venta_pago_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.venta_pago_id_venta_pago_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.venta_pago_id_venta_pago_seq OWNER TO postgres;

--
-- Name: venta_pago_id_venta_pago_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.venta_pago_id_venta_pago_seq OWNED BY public.venta_pago.id_venta_pago;


--
-- Name: autor_artista id_autor_artista; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.autor_artista ALTER COLUMN id_autor_artista SET DEFAULT nextval('public.autor_artista_id_autor_artista_seq'::regclass);


--
-- Name: categoria id_categoria; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.categoria ALTER COLUMN id_categoria SET DEFAULT nextval('public.categoria_id_categoria_seq'::regclass);


--
-- Name: detalle_venta id_detalle_venta; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.detalle_venta ALTER COLUMN id_detalle_venta SET DEFAULT nextval('public.detalle_venta_id_detalle_venta_seq'::regclass);


--
-- Name: editorial_sello id_editorial_sello; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.editorial_sello ALTER COLUMN id_editorial_sello SET DEFAULT nextval('public.editorial_sello_id_editorial_sello_seq'::regclass);


--
-- Name: estado_venta id_estado_venta; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.estado_venta ALTER COLUMN id_estado_venta SET DEFAULT nextval('public.estado_venta_id_estado_venta_seq'::regclass);


--
-- Name: forma_pago id_forma_pago; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.forma_pago ALTER COLUMN id_forma_pago SET DEFAULT nextval('public.forma_pago_id_forma_pago_seq'::regclass);


--
-- Name: historial_envio id_historial; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.historial_envio ALTER COLUMN id_historial SET DEFAULT nextval('public.historial_envio_id_historial_seq'::regclass);


--
-- Name: historial_precio id_historial; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.historial_precio ALTER COLUMN id_historial SET DEFAULT nextval('public.historial_precio_id_historial_seq'::regclass);


--
-- Name: movimiento_stock id_movimiento; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.movimiento_stock ALTER COLUMN id_movimiento SET DEFAULT nextval('public.movimiento_stock_id_movimiento_seq'::regclass);


--
-- Name: promocion id_promocion; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.promocion ALTER COLUMN id_promocion SET DEFAULT nextval('public.promocion_id_promocion_seq'::regclass);


--
-- Name: rango_etario id_rango_etario; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.rango_etario ALTER COLUMN id_rango_etario SET DEFAULT nextval('public.rango_etario_id_rango_etario_seq'::regclass);


--
-- Name: rol id_rol; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.rol ALTER COLUMN id_rol SET DEFAULT nextval('public.rol_id_rol_seq'::regclass);


--
-- Name: sucursal id_sucursal; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sucursal ALTER COLUMN id_sucursal SET DEFAULT nextval('public.sucursal_id_sucursal_seq'::regclass);


--
-- Name: tipo_producto id_tipo_producto; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tipo_producto ALTER COLUMN id_tipo_producto SET DEFAULT nextval('public.tipo_producto_id_tipo_producto_seq'::regclass);


--
-- Name: venta_pago id_venta_pago; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.venta_pago ALTER COLUMN id_venta_pago SET DEFAULT nextval('public.venta_pago_id_venta_pago_seq'::regclass);


--
-- Name: autor_artista autor_artista_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.autor_artista
    ADD CONSTRAINT autor_artista_pkey PRIMARY KEY (id_autor_artista);


--
-- Name: carrito_item carrito_item_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.carrito_item
    ADD CONSTRAINT carrito_item_pkey PRIMARY KEY (id_item);


--
-- Name: carrito carrito_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.carrito
    ADD CONSTRAINT carrito_pkey PRIMARY KEY (id_carrito);


--
-- Name: categoria categoria_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.categoria
    ADD CONSTRAINT categoria_pkey PRIMARY KEY (id_categoria);


--
-- Name: cliente cliente_id_persona_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cliente
    ADD CONSTRAINT cliente_id_persona_key UNIQUE (id_persona);


--
-- Name: cliente cliente_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cliente
    ADD CONSTRAINT cliente_pkey PRIMARY KEY (id_cliente);


--
-- Name: detalle_venta detalle_venta_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.detalle_venta
    ADD CONSTRAINT detalle_venta_pkey PRIMARY KEY (id_detalle_venta);


--
-- Name: editorial_sello editorial_sello_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.editorial_sello
    ADD CONSTRAINT editorial_sello_pkey PRIMARY KEY (id_editorial_sello);


--
-- Name: empleado empleado_id_persona_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.empleado
    ADD CONSTRAINT empleado_id_persona_key UNIQUE (id_persona);


--
-- Name: empleado empleado_legajo_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.empleado
    ADD CONSTRAINT empleado_legajo_key UNIQUE (legajo);


--
-- Name: empleado empleado_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.empleado
    ADD CONSTRAINT empleado_pkey PRIMARY KEY (id_empleado);


--
-- Name: envio envio_id_venta_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.envio
    ADD CONSTRAINT envio_id_venta_key UNIQUE (id_venta);


--
-- Name: envio envio_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.envio
    ADD CONSTRAINT envio_pkey PRIMARY KEY (id_envio);


--
-- Name: estado_venta estado_venta_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.estado_venta
    ADD CONSTRAINT estado_venta_pkey PRIMARY KEY (id_estado_venta);


--
-- Name: forma_pago forma_pago_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.forma_pago
    ADD CONSTRAINT forma_pago_pkey PRIMARY KEY (id_forma_pago);


--
-- Name: historial_envio historial_envio_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.historial_envio
    ADD CONSTRAINT historial_envio_pkey PRIMARY KEY (id_historial);


--
-- Name: historial_precio historial_precio_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.historial_precio
    ADD CONSTRAINT historial_precio_pkey PRIMARY KEY (id_historial);


--
-- Name: inventario inventario_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.inventario
    ADD CONSTRAINT inventario_pkey PRIMARY KEY (id_sucursal, id_producto);


--
-- Name: movimiento_stock movimiento_stock_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.movimiento_stock
    ADD CONSTRAINT movimiento_stock_pkey PRIMARY KEY (id_movimiento);


--
-- Name: persona persona_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.persona
    ADD CONSTRAINT persona_pkey PRIMARY KEY (id_persona);


--
-- Name: producto_autor producto_autor_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.producto_autor
    ADD CONSTRAINT producto_autor_pkey PRIMARY KEY (id_producto, id_autor_artista);


--
-- Name: producto_categoria producto_categoria_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.producto_categoria
    ADD CONSTRAINT producto_categoria_pkey PRIMARY KEY (id_producto, id_categoria);


--
-- Name: producto producto_codigo_barras_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.producto
    ADD CONSTRAINT producto_codigo_barras_key UNIQUE (codigo_barras);


--
-- Name: producto producto_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.producto
    ADD CONSTRAINT producto_pkey PRIMARY KEY (id_producto);


--
-- Name: promocion promocion_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.promocion
    ADD CONSTRAINT promocion_pkey PRIMARY KEY (id_promocion);


--
-- Name: rango_etario rango_etario_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.rango_etario
    ADD CONSTRAINT rango_etario_pkey PRIMARY KEY (id_rango_etario);


--
-- Name: rol rol_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.rol
    ADD CONSTRAINT rol_pkey PRIMARY KEY (id_rol);


--
-- Name: sucursal sucursal_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sucursal
    ADD CONSTRAINT sucursal_pkey PRIMARY KEY (id_sucursal);


--
-- Name: tipo_producto tipo_producto_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tipo_producto
    ADD CONSTRAINT tipo_producto_pkey PRIMARY KEY (id_tipo_producto);


--
-- Name: usuario usuario_email_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT usuario_email_key UNIQUE (email);


--
-- Name: usuario usuario_id_persona_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT usuario_id_persona_key UNIQUE (id_persona);


--
-- Name: usuario usuario_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT usuario_pkey PRIMARY KEY (id_usuario);


--
-- Name: venta_pago venta_pago_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.venta_pago
    ADD CONSTRAINT venta_pago_pkey PRIMARY KEY (id_venta_pago);


--
-- Name: venta venta_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.venta
    ADD CONSTRAINT venta_pkey PRIMARY KEY (id_venta);


--
-- Name: idx_carrito_cliente_activo; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_carrito_cliente_activo ON public.carrito USING btree (id_cliente, activo);


--
-- Name: idx_carrito_item_carrito; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_carrito_item_carrito ON public.carrito_item USING btree (id_carrito);


--
-- Name: idx_carrito_item_producto; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_carrito_item_producto ON public.carrito_item USING btree (id_producto);


--
-- Name: uq_carrito_cliente_activo; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX uq_carrito_cliente_activo ON public.carrito USING btree (id_cliente) WHERE (activo = true);


--
-- Name: uq_carrito_producto; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX uq_carrito_producto ON public.carrito_item USING btree (id_carrito, id_producto);


--
-- Name: carrito fk_carrito_cliente; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.carrito
    ADD CONSTRAINT fk_carrito_cliente FOREIGN KEY (id_cliente) REFERENCES public.cliente(id_cliente) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- Name: carrito_item fk_carrito_item_carrito; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.carrito_item
    ADD CONSTRAINT fk_carrito_item_carrito FOREIGN KEY (id_carrito) REFERENCES public.carrito(id_carrito) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: carrito_item fk_carrito_item_producto; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.carrito_item
    ADD CONSTRAINT fk_carrito_item_producto FOREIGN KEY (id_producto) REFERENCES public.producto(id_producto) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- Name: cliente fk_cliente_persona; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cliente
    ADD CONSTRAINT fk_cliente_persona FOREIGN KEY (id_persona) REFERENCES public.persona(id_persona);


--
-- Name: detalle_venta fk_detalle_producto; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.detalle_venta
    ADD CONSTRAINT fk_detalle_producto FOREIGN KEY (id_producto) REFERENCES public.producto(id_producto);


--
-- Name: detalle_venta fk_detalle_promo; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.detalle_venta
    ADD CONSTRAINT fk_detalle_promo FOREIGN KEY (id_promocion) REFERENCES public.promocion(id_promocion);


--
-- Name: detalle_venta fk_detalle_venta; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.detalle_venta
    ADD CONSTRAINT fk_detalle_venta FOREIGN KEY (id_venta) REFERENCES public.venta(id_venta);


--
-- Name: empleado fk_empleado_persona; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.empleado
    ADD CONSTRAINT fk_empleado_persona FOREIGN KEY (id_persona) REFERENCES public.persona(id_persona);


--
-- Name: empleado fk_empleado_sucursal; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.empleado
    ADD CONSTRAINT fk_empleado_sucursal FOREIGN KEY (id_sucursal) REFERENCES public.sucursal(id_sucursal);


--
-- Name: envio fk_envio_venta; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.envio
    ADD CONSTRAINT fk_envio_venta FOREIGN KEY (id_venta) REFERENCES public.venta(id_venta);


--
-- Name: historial_envio fk_historial_empleado; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.historial_envio
    ADD CONSTRAINT fk_historial_empleado FOREIGN KEY (id_empleado) REFERENCES public.empleado(id_empleado);


--
-- Name: historial_envio fk_historial_envio; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.historial_envio
    ADD CONSTRAINT fk_historial_envio FOREIGN KEY (id_envio) REFERENCES public.envio(id_envio);


--
-- Name: historial_precio fk_hp_empleado; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.historial_precio
    ADD CONSTRAINT fk_hp_empleado FOREIGN KEY (id_empleado) REFERENCES public.empleado(id_empleado);


--
-- Name: historial_precio fk_hp_producto; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.historial_precio
    ADD CONSTRAINT fk_hp_producto FOREIGN KEY (id_producto) REFERENCES public.producto(id_producto);


--
-- Name: inventario fk_inv_producto; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.inventario
    ADD CONSTRAINT fk_inv_producto FOREIGN KEY (id_producto) REFERENCES public.producto(id_producto);


--
-- Name: inventario fk_inv_sucursal; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.inventario
    ADD CONSTRAINT fk_inv_sucursal FOREIGN KEY (id_sucursal) REFERENCES public.sucursal(id_sucursal);


--
-- Name: movimiento_stock fk_mov_empleado; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.movimiento_stock
    ADD CONSTRAINT fk_mov_empleado FOREIGN KEY (id_empleado) REFERENCES public.empleado(id_empleado);


--
-- Name: movimiento_stock fk_mov_producto; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.movimiento_stock
    ADD CONSTRAINT fk_mov_producto FOREIGN KEY (id_producto) REFERENCES public.producto(id_producto);


--
-- Name: movimiento_stock fk_mov_sucursal; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.movimiento_stock
    ADD CONSTRAINT fk_mov_sucursal FOREIGN KEY (id_sucursal) REFERENCES public.sucursal(id_sucursal);


--
-- Name: producto_autor fk_pa_autor; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.producto_autor
    ADD CONSTRAINT fk_pa_autor FOREIGN KEY (id_autor_artista) REFERENCES public.autor_artista(id_autor_artista);


--
-- Name: producto_autor fk_pa_producto; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.producto_autor
    ADD CONSTRAINT fk_pa_producto FOREIGN KEY (id_producto) REFERENCES public.producto(id_producto);


--
-- Name: producto_categoria fk_pc_categoria; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.producto_categoria
    ADD CONSTRAINT fk_pc_categoria FOREIGN KEY (id_categoria) REFERENCES public.categoria(id_categoria);


--
-- Name: producto_categoria fk_pc_producto; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.producto_categoria
    ADD CONSTRAINT fk_pc_producto FOREIGN KEY (id_producto) REFERENCES public.producto(id_producto);


--
-- Name: producto fk_producto_editorial; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.producto
    ADD CONSTRAINT fk_producto_editorial FOREIGN KEY (id_editorial_sello) REFERENCES public.editorial_sello(id_editorial_sello);


--
-- Name: producto fk_producto_rango; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.producto
    ADD CONSTRAINT fk_producto_rango FOREIGN KEY (id_rango_etario) REFERENCES public.rango_etario(id_rango_etario);


--
-- Name: producto fk_producto_tipo; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.producto
    ADD CONSTRAINT fk_producto_tipo FOREIGN KEY (id_tipo_producto) REFERENCES public.tipo_producto(id_tipo_producto);


--
-- Name: usuario fk_usuario_persona; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT fk_usuario_persona FOREIGN KEY (id_persona) REFERENCES public.persona(id_persona);


--
-- Name: usuario fk_usuario_rol; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT fk_usuario_rol FOREIGN KEY (id_rol) REFERENCES public.rol(id_rol);


--
-- Name: venta fk_venta_cliente; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.venta
    ADD CONSTRAINT fk_venta_cliente FOREIGN KEY (id_cliente) REFERENCES public.cliente(id_cliente);


--
-- Name: venta fk_venta_empleado; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.venta
    ADD CONSTRAINT fk_venta_empleado FOREIGN KEY (id_empleado) REFERENCES public.empleado(id_empleado);


--
-- Name: venta fk_venta_estado; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.venta
    ADD CONSTRAINT fk_venta_estado FOREIGN KEY (id_estado_venta) REFERENCES public.estado_venta(id_estado_venta);


--
-- Name: venta fk_venta_sucursal; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.venta
    ADD CONSTRAINT fk_venta_sucursal FOREIGN KEY (id_sucursal) REFERENCES public.sucursal(id_sucursal);


--
-- Name: venta_pago fk_vp_pago; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.venta_pago
    ADD CONSTRAINT fk_vp_pago FOREIGN KEY (id_forma_pago) REFERENCES public.forma_pago(id_forma_pago);


--
-- Name: venta_pago fk_vp_venta; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.venta_pago
    ADD CONSTRAINT fk_vp_venta FOREIGN KEY (id_venta) REFERENCES public.venta(id_venta);


