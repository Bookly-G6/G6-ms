
-- Dumped from database version 18.4
-- Dumped by pg_dump version 18.4

-- Started on 2026-06-20 22:19:53

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
-- TOC entry 236 (class 1259 OID 16873)
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
-- TOC entry 235 (class 1259 OID 16872)
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
-- TOC entry 5183 (class 0 OID 0)
-- Dependencies: 235
-- Name: autor_artista_id_autor_artista_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.autor_artista_id_autor_artista_seq OWNED BY public.autor_artista.id_autor_artista;


--
-- TOC entry 228 (class 1259 OID 16834)
-- Name: categoria; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.categoria (
    id_categoria integer NOT NULL,
    nombre_categoria character varying(255) NOT NULL,
    activa boolean DEFAULT true
);


ALTER TABLE public.categoria OWNER TO postgres;

--
-- TOC entry 227 (class 1259 OID 16833)
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
-- TOC entry 5184 (class 0 OID 0)
-- Dependencies: 227
-- Name: categoria_id_categoria_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.categoria_id_categoria_seq OWNED BY public.categoria.id_categoria;


--
-- TOC entry 226 (class 1259 OID 16823)
-- Name: cliente; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.cliente (
    id_cliente uuid NOT NULL,
    id_persona uuid NOT NULL,
    puntos_fidelidad integer DEFAULT 0
);


ALTER TABLE public.cliente OWNER TO postgres;

--
-- TOC entry 255 (class 1259 OID 17003)
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
-- TOC entry 254 (class 1259 OID 17002)
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
-- TOC entry 5185 (class 0 OID 0)
-- Dependencies: 254
-- Name: detalle_venta_id_detalle_venta_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.detalle_venta_id_detalle_venta_seq OWNED BY public.detalle_venta.id_detalle_venta;


--
-- TOC entry 232 (class 1259 OID 16854)
-- Name: editorial_sello; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.editorial_sello (
    id_editorial_sello integer NOT NULL,
    nombre character varying(255) NOT NULL,
    activa boolean DEFAULT true
);


ALTER TABLE public.editorial_sello OWNER TO postgres;

--
-- TOC entry 231 (class 1259 OID 16853)
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
-- TOC entry 5186 (class 0 OID 0)
-- Dependencies: 231
-- Name: editorial_sello_id_editorial_sello_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.editorial_sello_id_editorial_sello_seq OWNED BY public.editorial_sello.id_editorial_sello;


--
-- TOC entry 225 (class 1259 OID 16811)
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
-- TOC entry 256 (class 1259 OID 17156)
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
-- TOC entry 250 (class 1259 OID 16971)
-- Name: estado_venta; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.estado_venta (
    id_estado_venta integer NOT NULL,
    nombre_estado character varying(255) NOT NULL
);


ALTER TABLE public.estado_venta OWNER TO postgres;

--
-- TOC entry 249 (class 1259 OID 16970)
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
-- TOC entry 5187 (class 0 OID 0)
-- Dependencies: 249
-- Name: estado_venta_id_estado_venta_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.estado_venta_id_estado_venta_seq OWNED BY public.estado_venta.id_estado_venta;


--
-- TOC entry 248 (class 1259 OID 16962)
-- Name: forma_pago; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.forma_pago (
    id_forma_pago integer NOT NULL,
    nombre_pago character varying(255) NOT NULL
);


ALTER TABLE public.forma_pago OWNER TO postgres;

--
-- TOC entry 247 (class 1259 OID 16961)
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
-- TOC entry 5188 (class 0 OID 0)
-- Dependencies: 247
-- Name: forma_pago_id_forma_pago_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.forma_pago_id_forma_pago_seq OWNED BY public.forma_pago.id_forma_pago;


--
-- TOC entry 258 (class 1259 OID 17177)
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
-- TOC entry 257 (class 1259 OID 17176)
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
-- TOC entry 5189 (class 0 OID 0)
-- Dependencies: 257
-- Name: historial_envio_id_historial_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.historial_envio_id_historial_seq OWNED BY public.historial_envio.id_historial;


--
-- TOC entry 246 (class 1259 OID 16949)
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
-- TOC entry 245 (class 1259 OID 16948)
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
-- TOC entry 5190 (class 0 OID 0)
-- Dependencies: 245
-- Name: historial_precio_id_historial_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.historial_precio_id_historial_seq OWNED BY public.historial_precio.id_historial;


--
-- TOC entry 240 (class 1259 OID 16915)
-- Name: inventario; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.inventario (
    id_sucursal integer NOT NULL,
    id_producto uuid NOT NULL,
    stock integer DEFAULT 0 NOT NULL
);


ALTER TABLE public.inventario OWNER TO postgres;

--
-- TOC entry 242 (class 1259 OID 16925)
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
-- TOC entry 241 (class 1259 OID 16924)
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
-- TOC entry 5191 (class 0 OID 0)
-- Dependencies: 241
-- Name: movimiento_stock_id_movimiento_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.movimiento_stock_id_movimiento_seq OWNED BY public.movimiento_stock.id_movimiento;


--
-- TOC entry 223 (class 1259 OID 16784)
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
-- TOC entry 237 (class 1259 OID 16884)
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
-- TOC entry 239 (class 1259 OID 16908)
-- Name: producto_autor; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.producto_autor (
    id_producto uuid NOT NULL,
    id_autor_artista integer NOT NULL
);


ALTER TABLE public.producto_autor OWNER TO postgres;

--
-- TOC entry 238 (class 1259 OID 16901)
-- Name: producto_categoria; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.producto_categoria (
    id_producto uuid NOT NULL,
    id_categoria integer NOT NULL
);


ALTER TABLE public.producto_categoria OWNER TO postgres;

--
-- TOC entry 244 (class 1259 OID 16939)
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
-- TOC entry 243 (class 1259 OID 16938)
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
-- TOC entry 5192 (class 0 OID 0)
-- Dependencies: 243
-- Name: promocion_id_promocion_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.promocion_id_promocion_seq OWNED BY public.promocion.id_promocion;


--
-- TOC entry 234 (class 1259 OID 16864)
-- Name: rango_etario; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.rango_etario (
    id_rango_etario integer NOT NULL,
    descripcion character varying(255) NOT NULL
);


ALTER TABLE public.rango_etario OWNER TO postgres;

--
-- TOC entry 233 (class 1259 OID 16863)
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
-- TOC entry 5193 (class 0 OID 0)
-- Dependencies: 233
-- Name: rango_etario_id_rango_etario_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.rango_etario_id_rango_etario_seq OWNED BY public.rango_etario.id_rango_etario;


--
-- TOC entry 220 (class 1259 OID 16764)
-- Name: rol; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.rol (
    id_rol integer NOT NULL,
    nombre_rol character varying(255) NOT NULL
);


ALTER TABLE public.rol OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 16763)
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
-- TOC entry 5194 (class 0 OID 0)
-- Dependencies: 219
-- Name: rol_id_rol_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.rol_id_rol_seq OWNED BY public.rol.id_rol;


--
-- TOC entry 222 (class 1259 OID 16773)
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
-- TOC entry 221 (class 1259 OID 16772)
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
-- TOC entry 5195 (class 0 OID 0)
-- Dependencies: 221
-- Name: sucursal_id_sucursal_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.sucursal_id_sucursal_seq OWNED BY public.sucursal.id_sucursal;


--
-- TOC entry 230 (class 1259 OID 16844)
-- Name: tipo_producto; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tipo_producto (
    id_tipo_producto integer NOT NULL,
    nombre_tipo character varying(255) NOT NULL,
    activa boolean DEFAULT true
);


ALTER TABLE public.tipo_producto OWNER TO postgres;

--
-- TOC entry 229 (class 1259 OID 16843)
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
-- TOC entry 5196 (class 0 OID 0)
-- Dependencies: 229
-- Name: tipo_producto_id_tipo_producto_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.tipo_producto_id_tipo_producto_seq OWNED BY public.tipo_producto.id_tipo_producto;


--
-- TOC entry 224 (class 1259 OID 16794)
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
-- TOC entry 251 (class 1259 OID 16979)
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
-- TOC entry 253 (class 1259 OID 16992)
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
-- TOC entry 252 (class 1259 OID 16991)
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
-- TOC entry 5197 (class 0 OID 0)
-- Dependencies: 252
-- Name: venta_pago_id_venta_pago_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.venta_pago_id_venta_pago_seq OWNED BY public.venta_pago.id_venta_pago;


--
-- TOC entry 4877 (class 2604 OID 16876)
-- Name: autor_artista id_autor_artista; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.autor_artista ALTER COLUMN id_autor_artista SET DEFAULT nextval('public.autor_artista_id_autor_artista_seq'::regclass);


--
-- TOC entry 4870 (class 2604 OID 16837)
-- Name: categoria id_categoria; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.categoria ALTER COLUMN id_categoria SET DEFAULT nextval('public.categoria_id_categoria_seq'::regclass);


--
-- TOC entry 4891 (class 2604 OID 17006)
-- Name: detalle_venta id_detalle_venta; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.detalle_venta ALTER COLUMN id_detalle_venta SET DEFAULT nextval('public.detalle_venta_id_detalle_venta_seq'::regclass);


--
-- TOC entry 4874 (class 2604 OID 16857)
-- Name: editorial_sello id_editorial_sello; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.editorial_sello ALTER COLUMN id_editorial_sello SET DEFAULT nextval('public.editorial_sello_id_editorial_sello_seq'::regclass);


--
-- TOC entry 4888 (class 2604 OID 16974)
-- Name: estado_venta id_estado_venta; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.estado_venta ALTER COLUMN id_estado_venta SET DEFAULT nextval('public.estado_venta_id_estado_venta_seq'::regclass);


--
-- TOC entry 4887 (class 2604 OID 16965)
-- Name: forma_pago id_forma_pago; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.forma_pago ALTER COLUMN id_forma_pago SET DEFAULT nextval('public.forma_pago_id_forma_pago_seq'::regclass);


--
-- TOC entry 4894 (class 2604 OID 17180)
-- Name: historial_envio id_historial; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.historial_envio ALTER COLUMN id_historial SET DEFAULT nextval('public.historial_envio_id_historial_seq'::regclass);


--
-- TOC entry 4885 (class 2604 OID 16952)
-- Name: historial_precio id_historial; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.historial_precio ALTER COLUMN id_historial SET DEFAULT nextval('public.historial_precio_id_historial_seq'::regclass);


--
-- TOC entry 4881 (class 2604 OID 16928)
-- Name: movimiento_stock id_movimiento; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.movimiento_stock ALTER COLUMN id_movimiento SET DEFAULT nextval('public.movimiento_stock_id_movimiento_seq'::regclass);


--
-- TOC entry 4883 (class 2604 OID 16942)
-- Name: promocion id_promocion; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.promocion ALTER COLUMN id_promocion SET DEFAULT nextval('public.promocion_id_promocion_seq'::regclass);


--
-- TOC entry 4876 (class 2604 OID 16867)
-- Name: rango_etario id_rango_etario; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.rango_etario ALTER COLUMN id_rango_etario SET DEFAULT nextval('public.rango_etario_id_rango_etario_seq'::regclass);


--
-- TOC entry 4865 (class 2604 OID 16767)
-- Name: rol id_rol; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.rol ALTER COLUMN id_rol SET DEFAULT nextval('public.rol_id_rol_seq'::regclass);


--
-- TOC entry 4866 (class 2604 OID 16776)
-- Name: sucursal id_sucursal; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sucursal ALTER COLUMN id_sucursal SET DEFAULT nextval('public.sucursal_id_sucursal_seq'::regclass);


--
-- TOC entry 4872 (class 2604 OID 16847)
-- Name: tipo_producto id_tipo_producto; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tipo_producto ALTER COLUMN id_tipo_producto SET DEFAULT nextval('public.tipo_producto_id_tipo_producto_seq'::regclass);


--
-- TOC entry 4890 (class 2604 OID 16995)
-- Name: venta_pago id_venta_pago; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.venta_pago ALTER COLUMN id_venta_pago SET DEFAULT nextval('public.venta_pago_id_venta_pago_seq'::regclass);


--
-- TOC entry 5155 (class 0 OID 16873)
-- Dependencies: 236
-- Data for Name: autor_artista; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.autor_artista (id_autor_artista, nombre, biografia, activa) FROM stdin;
2	Robert C. Martin	Conocido como Uncle Bob, referente en arquitectura de software.	t
4	Stephen King	Maestro del terror literario.	t
5	J.K. Rowling	Creadora del universo de Harry Potter.	t
1	Stephen King	Maestro del terror y suspenso.	f
3	Robert C. Martin	Ingeniero de software y autor de Clean Code.	f
\.


--
-- TOC entry 5147 (class 0 OID 16834)
-- Dependencies: 228
-- Data for Name: categoria; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.categoria (id_categoria, nombre_categoria, activa) FROM stdin;
1	Ficción	t
2	Programación	t
3	Terror	t
4	Programación y Tecnología	t
5	Ficción y Fantasía	t
6	Terror	t
\.


--
-- TOC entry 5145 (class 0 OID 16823)
-- Dependencies: 226
-- Data for Name: cliente; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.cliente (id_cliente, id_persona, puntos_fidelidad) FROM stdin;
b4b4b4b4-b4b4-b4b4-b4b4-b4b4b4b4b4b4	c3c3c3c3-c3c3-c3c3-c3c3-c3c3c3c3c3c3	0
\.


--
-- TOC entry 5174 (class 0 OID 17003)
-- Dependencies: 255
-- Data for Name: detalle_venta; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.detalle_venta (id_detalle_venta, id_venta, id_producto, cantidad, precio_unitario, id_promocion, subtotal_renglon) FROM stdin;
\.


--
-- TOC entry 5151 (class 0 OID 16854)
-- Dependencies: 232
-- Data for Name: editorial_sello; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.editorial_sello (id_editorial_sello, nombre, activa) FROM stdin;
1	Penguin Random House	t
2	O'Reilly Media	t
3	Planeta	t
4	Penguin Random House	t
5	O'Reilly Media	t
6	Ivrea	t
\.


--
-- TOC entry 5144 (class 0 OID 16811)
-- Dependencies: 225
-- Data for Name: empleado; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.empleado (id_empleado, id_persona, legajo, cargo, id_sucursal) FROM stdin;
e2e2e2e2-e2e2-e2e2-e2e2-e2e2e2e2e2e2	a1a1a1a1-a1a1-a1a1-a1a1-a1a1a1a1a1a1	\N	\N	1
\.


--
-- TOC entry 5175 (class 0 OID 17156)
-- Dependencies: 256
-- Data for Name: envio; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.envio (id_envio, id_venta, tipo_envio, estado_logistica, empresa_correo, numero_tracking, codigo_retiro, observaciones, fecha_actualizacion, activo) FROM stdin;
b7f80904-7c7d-4c8d-a2fc-e8265eafc292	77777777-7777-7777-7777-777777777777	DOMICILIO	DESPACHADO	Andreani	AR-987654321X	\N	El timbre no funciona bien, golpear las manos por favor.	2026-06-20 21:45:40.649815	t
\.


--
-- TOC entry 5169 (class 0 OID 16971)
-- Dependencies: 250
-- Data for Name: estado_venta; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.estado_venta (id_estado_venta, nombre_estado) FROM stdin;
1	CONFIRMADA
\.


--
-- TOC entry 5167 (class 0 OID 16962)
-- Dependencies: 248
-- Data for Name: forma_pago; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.forma_pago (id_forma_pago, nombre_pago) FROM stdin;
\.


--
-- TOC entry 5177 (class 0 OID 17177)
-- Dependencies: 258
-- Data for Name: historial_envio; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.historial_envio (id_historial, id_envio, estado_anterior, estado_nuevo, fecha_cambio, observaciones, id_empleado) FROM stdin;
1	b7f80904-7c7d-4c8d-a2fc-e8265eafc292	\N	EN_PREPARACION	2026-06-20 21:45:40.660785	Envío inicializado por el sistema	\N
2	b7f80904-7c7d-4c8d-a2fc-e8265eafc292	EN_PREPARACION	DESPACHADO	2026-06-20 21:46:33.250767	Cambio de estado logístico	e2e2e2e2-e2e2-e2e2-e2e2-e2e2e2e2e2e2
\.


--
-- TOC entry 5165 (class 0 OID 16949)
-- Dependencies: 246
-- Data for Name: historial_precio; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.historial_precio (id_historial, id_producto, precio_costo_anterior, precio_costo_nuevo, precio_venta_anterior, precio_venta_nuevo, fecha_cambio, id_empleado) FROM stdin;
\.


--
-- TOC entry 5159 (class 0 OID 16915)
-- Dependencies: 240
-- Data for Name: inventario; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.inventario (id_sucursal, id_producto, stock) FROM stdin;
\.


--
-- TOC entry 5161 (class 0 OID 16925)
-- Dependencies: 242
-- Data for Name: movimiento_stock; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.movimiento_stock (id_movimiento, id_sucursal, id_producto, cantidad, tipo_movimiento, fecha, id_empleado) FROM stdin;
\.


--
-- TOC entry 5142 (class 0 OID 16784)
-- Dependencies: 223
-- Data for Name: persona; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.persona (id_persona, nombre, apellido, dni, telefono) FROM stdin;
a1a1a1a1-a1a1-a1a1-a1a1-a1a1a1a1a1a1	Carlos	Logístico	20111222	\N
c3c3c3c3-c3c3-c3c3-c3c3-c3c3c3c3c3c3	Lucía	Compradora	30444555	\N
\.


--
-- TOC entry 5156 (class 0 OID 16884)
-- Dependencies: 237
-- Data for Name: producto; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.producto (id_producto, codigo_barras, nombre_producto, descripcion, precio_costo, precio_actual, activo, id_tipo_producto, id_editorial_sello, id_rango_etario, atributos_especificos) FROM stdin;
7ce9f235-3215-4ab9-b3a0-a31e3d9b6a53	9780132350884	Clean Code: A Handbook of Agile Software Craftsmanship	El libro definitivo para aprender a escribir código limpio, mantenible y escalable.	12000.50	28500.00	f	1	2	3	{"tapa": "blanda", "idioma": "español", "paginas": 464, "edicion_limitada": false}
\.


--
-- TOC entry 5158 (class 0 OID 16908)
-- Dependencies: 239
-- Data for Name: producto_autor; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.producto_autor (id_producto, id_autor_artista) FROM stdin;
7ce9f235-3215-4ab9-b3a0-a31e3d9b6a53	1
\.


--
-- TOC entry 5157 (class 0 OID 16901)
-- Dependencies: 238
-- Data for Name: producto_categoria; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.producto_categoria (id_producto, id_categoria) FROM stdin;
7ce9f235-3215-4ab9-b3a0-a31e3d9b6a53	1
\.


--
-- TOC entry 5163 (class 0 OID 16939)
-- Dependencies: 244
-- Data for Name: promocion; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.promocion (id_promocion, nombre_promo, porcentaje_descuento, activa) FROM stdin;
\.


--
-- TOC entry 5153 (class 0 OID 16864)
-- Dependencies: 234
-- Data for Name: rango_etario; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.rango_etario (id_rango_etario, descripcion) FROM stdin;
1	Adultos
2	Jóvenes/Adolescentes
3	Infantil
4	Infantil (0-12 años)
5	Juvenil (13-17 años)
6	Adultos (18+)
\.


--
-- TOC entry 5139 (class 0 OID 16764)
-- Dependencies: 220
-- Data for Name: rol; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.rol (id_rol, nombre_rol) FROM stdin;
\.


--
-- TOC entry 5141 (class 0 OID 16773)
-- Dependencies: 222
-- Data for Name: sucursal; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.sucursal (id_sucursal, nombre, direccion, activa) FROM stdin;
1	Sucursal Central Formosa	\N	t
\.


--
-- TOC entry 5149 (class 0 OID 16844)
-- Dependencies: 230
-- Data for Name: tipo_producto; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.tipo_producto (id_tipo_producto, nombre_tipo, activa) FROM stdin;
1	Libro Físico	t
2	E-book	t
3	Audiolibro	t
4	Libro Físico	t
5	E-book	t
6	Manga	t
\.


--
-- TOC entry 5143 (class 0 OID 16794)
-- Dependencies: 224
-- Data for Name: usuario; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.usuario (id_usuario, id_persona, email, password, id_rol, activo) FROM stdin;
\.


--
-- TOC entry 5170 (class 0 OID 16979)
-- Dependencies: 251
-- Data for Name: venta; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.venta (id_venta, fecha, subtotal_sin_descuentos, total_final, origen_venta, id_estado_venta, id_sucursal, id_cliente, id_empleado) FROM stdin;
77777777-7777-7777-7777-777777777777	2026-06-20 21:44:40.757311	0.00	0.00	WEB	1	1	b4b4b4b4-b4b4-b4b4-b4b4-b4b4b4b4b4b4	e2e2e2e2-e2e2-e2e2-e2e2-e2e2e2e2e2e2
\.


--
-- TOC entry 5172 (class 0 OID 16992)
-- Dependencies: 253
-- Data for Name: venta_pago; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.venta_pago (id_venta_pago, id_venta, id_forma_pago, monto_abonado) FROM stdin;
\.


--
-- TOC entry 5198 (class 0 OID 0)
-- Dependencies: 235
-- Name: autor_artista_id_autor_artista_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.autor_artista_id_autor_artista_seq', 5, true);


--
-- TOC entry 5199 (class 0 OID 0)
-- Dependencies: 227
-- Name: categoria_id_categoria_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.categoria_id_categoria_seq', 6, true);


--
-- TOC entry 5200 (class 0 OID 0)
-- Dependencies: 254
-- Name: detalle_venta_id_detalle_venta_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.detalle_venta_id_detalle_venta_seq', 1, false);


--
-- TOC entry 5201 (class 0 OID 0)
-- Dependencies: 231
-- Name: editorial_sello_id_editorial_sello_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.editorial_sello_id_editorial_sello_seq', 6, true);


--
-- TOC entry 5202 (class 0 OID 0)
-- Dependencies: 249
-- Name: estado_venta_id_estado_venta_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.estado_venta_id_estado_venta_seq', 1, false);


--
-- TOC entry 5203 (class 0 OID 0)
-- Dependencies: 247
-- Name: forma_pago_id_forma_pago_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.forma_pago_id_forma_pago_seq', 1, false);


--
-- TOC entry 5204 (class 0 OID 0)
-- Dependencies: 257
-- Name: historial_envio_id_historial_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.historial_envio_id_historial_seq', 2, true);


--
-- TOC entry 5205 (class 0 OID 0)
-- Dependencies: 245
-- Name: historial_precio_id_historial_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.historial_precio_id_historial_seq', 1, false);


--
-- TOC entry 5206 (class 0 OID 0)
-- Dependencies: 241
-- Name: movimiento_stock_id_movimiento_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.movimiento_stock_id_movimiento_seq', 1, false);


--
-- TOC entry 5207 (class 0 OID 0)
-- Dependencies: 243
-- Name: promocion_id_promocion_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.promocion_id_promocion_seq', 1, false);


--
-- TOC entry 5208 (class 0 OID 0)
-- Dependencies: 233
-- Name: rango_etario_id_rango_etario_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.rango_etario_id_rango_etario_seq', 6, true);


--
-- TOC entry 5209 (class 0 OID 0)
-- Dependencies: 219
-- Name: rol_id_rol_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.rol_id_rol_seq', 1, false);


--
-- TOC entry 5210 (class 0 OID 0)
-- Dependencies: 221
-- Name: sucursal_id_sucursal_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.sucursal_id_sucursal_seq', 1, false);


--
-- TOC entry 5211 (class 0 OID 0)
-- Dependencies: 229
-- Name: tipo_producto_id_tipo_producto_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.tipo_producto_id_tipo_producto_seq', 6, true);


--
-- TOC entry 5212 (class 0 OID 0)
-- Dependencies: 252
-- Name: venta_pago_id_venta_pago_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.venta_pago_id_venta_pago_seq', 1, false);


--
-- TOC entry 4927 (class 2606 OID 16883)
-- Name: autor_artista autor_artista_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.autor_artista
    ADD CONSTRAINT autor_artista_pkey PRIMARY KEY (id_autor_artista);


--
-- TOC entry 4919 (class 2606 OID 16842)
-- Name: categoria categoria_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.categoria
    ADD CONSTRAINT categoria_pkey PRIMARY KEY (id_categoria);


--
-- TOC entry 4915 (class 2606 OID 16832)
-- Name: cliente cliente_id_persona_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cliente
    ADD CONSTRAINT cliente_id_persona_key UNIQUE (id_persona);


--
-- TOC entry 4917 (class 2606 OID 16830)
-- Name: cliente cliente_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cliente
    ADD CONSTRAINT cliente_pkey PRIMARY KEY (id_cliente);


--
-- TOC entry 4953 (class 2606 OID 17014)
-- Name: detalle_venta detalle_venta_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.detalle_venta
    ADD CONSTRAINT detalle_venta_pkey PRIMARY KEY (id_detalle_venta);


--
-- TOC entry 4923 (class 2606 OID 16862)
-- Name: editorial_sello editorial_sello_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.editorial_sello
    ADD CONSTRAINT editorial_sello_pkey PRIMARY KEY (id_editorial_sello);


--
-- TOC entry 4909 (class 2606 OID 16820)
-- Name: empleado empleado_id_persona_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.empleado
    ADD CONSTRAINT empleado_id_persona_key UNIQUE (id_persona);


--
-- TOC entry 4911 (class 2606 OID 16822)
-- Name: empleado empleado_legajo_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.empleado
    ADD CONSTRAINT empleado_legajo_key UNIQUE (legajo);


--
-- TOC entry 4913 (class 2606 OID 16818)
-- Name: empleado empleado_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.empleado
    ADD CONSTRAINT empleado_pkey PRIMARY KEY (id_empleado);


--
-- TOC entry 4955 (class 2606 OID 17170)
-- Name: envio envio_id_venta_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.envio
    ADD CONSTRAINT envio_id_venta_key UNIQUE (id_venta);


--
-- TOC entry 4957 (class 2606 OID 17168)
-- Name: envio envio_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.envio
    ADD CONSTRAINT envio_pkey PRIMARY KEY (id_envio);


--
-- TOC entry 4947 (class 2606 OID 16978)
-- Name: estado_venta estado_venta_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.estado_venta
    ADD CONSTRAINT estado_venta_pkey PRIMARY KEY (id_estado_venta);


--
-- TOC entry 4945 (class 2606 OID 16969)
-- Name: forma_pago forma_pago_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.forma_pago
    ADD CONSTRAINT forma_pago_pkey PRIMARY KEY (id_forma_pago);


--
-- TOC entry 4959 (class 2606 OID 17188)
-- Name: historial_envio historial_envio_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.historial_envio
    ADD CONSTRAINT historial_envio_pkey PRIMARY KEY (id_historial);


--
-- TOC entry 4943 (class 2606 OID 16960)
-- Name: historial_precio historial_precio_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.historial_precio
    ADD CONSTRAINT historial_precio_pkey PRIMARY KEY (id_historial);


--
-- TOC entry 4937 (class 2606 OID 16923)
-- Name: inventario inventario_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.inventario
    ADD CONSTRAINT inventario_pkey PRIMARY KEY (id_sucursal, id_producto);


--
-- TOC entry 4939 (class 2606 OID 16937)
-- Name: movimiento_stock movimiento_stock_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.movimiento_stock
    ADD CONSTRAINT movimiento_stock_pkey PRIMARY KEY (id_movimiento);


--
-- TOC entry 4901 (class 2606 OID 16793)
-- Name: persona persona_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.persona
    ADD CONSTRAINT persona_pkey PRIMARY KEY (id_persona);


--
-- TOC entry 4935 (class 2606 OID 16914)
-- Name: producto_autor producto_autor_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.producto_autor
    ADD CONSTRAINT producto_autor_pkey PRIMARY KEY (id_producto, id_autor_artista);


--
-- TOC entry 4933 (class 2606 OID 16907)
-- Name: producto_categoria producto_categoria_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.producto_categoria
    ADD CONSTRAINT producto_categoria_pkey PRIMARY KEY (id_producto, id_categoria);


--
-- TOC entry 4929 (class 2606 OID 16900)
-- Name: producto producto_codigo_barras_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.producto
    ADD CONSTRAINT producto_codigo_barras_key UNIQUE (codigo_barras);


--
-- TOC entry 4931 (class 2606 OID 16898)
-- Name: producto producto_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.producto
    ADD CONSTRAINT producto_pkey PRIMARY KEY (id_producto);


--
-- TOC entry 4941 (class 2606 OID 16947)
-- Name: promocion promocion_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.promocion
    ADD CONSTRAINT promocion_pkey PRIMARY KEY (id_promocion);


--
-- TOC entry 4925 (class 2606 OID 16871)
-- Name: rango_etario rango_etario_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.rango_etario
    ADD CONSTRAINT rango_etario_pkey PRIMARY KEY (id_rango_etario);


--
-- TOC entry 4897 (class 2606 OID 16771)
-- Name: rol rol_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.rol
    ADD CONSTRAINT rol_pkey PRIMARY KEY (id_rol);


--
-- TOC entry 4899 (class 2606 OID 16783)
-- Name: sucursal sucursal_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sucursal
    ADD CONSTRAINT sucursal_pkey PRIMARY KEY (id_sucursal);


--
-- TOC entry 4921 (class 2606 OID 16852)
-- Name: tipo_producto tipo_producto_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tipo_producto
    ADD CONSTRAINT tipo_producto_pkey PRIMARY KEY (id_tipo_producto);


--
-- TOC entry 4903 (class 2606 OID 16810)
-- Name: usuario usuario_email_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT usuario_email_key UNIQUE (email);


--
-- TOC entry 4905 (class 2606 OID 16808)
-- Name: usuario usuario_id_persona_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT usuario_id_persona_key UNIQUE (id_persona);


--
-- TOC entry 4907 (class 2606 OID 16806)
-- Name: usuario usuario_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT usuario_pkey PRIMARY KEY (id_usuario);


--
-- TOC entry 4951 (class 2606 OID 17001)
-- Name: venta_pago venta_pago_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.venta_pago
    ADD CONSTRAINT venta_pago_pkey PRIMARY KEY (id_venta_pago);


--
-- TOC entry 4949 (class 2606 OID 16990)
-- Name: venta venta_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.venta
    ADD CONSTRAINT venta_pkey PRIMARY KEY (id_venta);


--
-- TOC entry 4964 (class 2606 OID 17035)
-- Name: cliente fk_cliente_persona; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cliente
    ADD CONSTRAINT fk_cliente_persona FOREIGN KEY (id_persona) REFERENCES public.persona(id_persona);


--
-- TOC entry 4985 (class 2606 OID 17135)
-- Name: detalle_venta fk_detalle_producto; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.detalle_venta
    ADD CONSTRAINT fk_detalle_producto FOREIGN KEY (id_producto) REFERENCES public.producto(id_producto);


--
-- TOC entry 4986 (class 2606 OID 17140)
-- Name: detalle_venta fk_detalle_promo; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.detalle_venta
    ADD CONSTRAINT fk_detalle_promo FOREIGN KEY (id_promocion) REFERENCES public.promocion(id_promocion);


--
-- TOC entry 4987 (class 2606 OID 17130)
-- Name: detalle_venta fk_detalle_venta; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.detalle_venta
    ADD CONSTRAINT fk_detalle_venta FOREIGN KEY (id_venta) REFERENCES public.venta(id_venta);


--
-- TOC entry 4962 (class 2606 OID 17025)
-- Name: empleado fk_empleado_persona; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.empleado
    ADD CONSTRAINT fk_empleado_persona FOREIGN KEY (id_persona) REFERENCES public.persona(id_persona);


--
-- TOC entry 4963 (class 2606 OID 17030)
-- Name: empleado fk_empleado_sucursal; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.empleado
    ADD CONSTRAINT fk_empleado_sucursal FOREIGN KEY (id_sucursal) REFERENCES public.sucursal(id_sucursal);


--
-- TOC entry 4988 (class 2606 OID 17171)
-- Name: envio fk_envio_venta; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.envio
    ADD CONSTRAINT fk_envio_venta FOREIGN KEY (id_venta) REFERENCES public.venta(id_venta);


--
-- TOC entry 4989 (class 2606 OID 17194)
-- Name: historial_envio fk_historial_empleado; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.historial_envio
    ADD CONSTRAINT fk_historial_empleado FOREIGN KEY (id_empleado) REFERENCES public.empleado(id_empleado);


--
-- TOC entry 4990 (class 2606 OID 17189)
-- Name: historial_envio fk_historial_envio; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.historial_envio
    ADD CONSTRAINT fk_historial_envio FOREIGN KEY (id_envio) REFERENCES public.envio(id_envio);


--
-- TOC entry 4977 (class 2606 OID 17105)
-- Name: historial_precio fk_hp_empleado; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.historial_precio
    ADD CONSTRAINT fk_hp_empleado FOREIGN KEY (id_empleado) REFERENCES public.empleado(id_empleado);


--
-- TOC entry 4978 (class 2606 OID 17100)
-- Name: historial_precio fk_hp_producto; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.historial_precio
    ADD CONSTRAINT fk_hp_producto FOREIGN KEY (id_producto) REFERENCES public.producto(id_producto);


--
-- TOC entry 4972 (class 2606 OID 17080)
-- Name: inventario fk_inv_producto; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.inventario
    ADD CONSTRAINT fk_inv_producto FOREIGN KEY (id_producto) REFERENCES public.producto(id_producto);


--
-- TOC entry 4973 (class 2606 OID 17075)
-- Name: inventario fk_inv_sucursal; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.inventario
    ADD CONSTRAINT fk_inv_sucursal FOREIGN KEY (id_sucursal) REFERENCES public.sucursal(id_sucursal);


--
-- TOC entry 4974 (class 2606 OID 17095)
-- Name: movimiento_stock fk_mov_empleado; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.movimiento_stock
    ADD CONSTRAINT fk_mov_empleado FOREIGN KEY (id_empleado) REFERENCES public.empleado(id_empleado);


--
-- TOC entry 4975 (class 2606 OID 17090)
-- Name: movimiento_stock fk_mov_producto; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.movimiento_stock
    ADD CONSTRAINT fk_mov_producto FOREIGN KEY (id_producto) REFERENCES public.producto(id_producto);


--
-- TOC entry 4976 (class 2606 OID 17085)
-- Name: movimiento_stock fk_mov_sucursal; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.movimiento_stock
    ADD CONSTRAINT fk_mov_sucursal FOREIGN KEY (id_sucursal) REFERENCES public.sucursal(id_sucursal);


--
-- TOC entry 4970 (class 2606 OID 17070)
-- Name: producto_autor fk_pa_autor; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.producto_autor
    ADD CONSTRAINT fk_pa_autor FOREIGN KEY (id_autor_artista) REFERENCES public.autor_artista(id_autor_artista);


--
-- TOC entry 4971 (class 2606 OID 17065)
-- Name: producto_autor fk_pa_producto; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.producto_autor
    ADD CONSTRAINT fk_pa_producto FOREIGN KEY (id_producto) REFERENCES public.producto(id_producto);


--
-- TOC entry 4968 (class 2606 OID 17060)
-- Name: producto_categoria fk_pc_categoria; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.producto_categoria
    ADD CONSTRAINT fk_pc_categoria FOREIGN KEY (id_categoria) REFERENCES public.categoria(id_categoria);


--
-- TOC entry 4969 (class 2606 OID 17055)
-- Name: producto_categoria fk_pc_producto; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.producto_categoria
    ADD CONSTRAINT fk_pc_producto FOREIGN KEY (id_producto) REFERENCES public.producto(id_producto);


--
-- TOC entry 4965 (class 2606 OID 17045)
-- Name: producto fk_producto_editorial; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.producto
    ADD CONSTRAINT fk_producto_editorial FOREIGN KEY (id_editorial_sello) REFERENCES public.editorial_sello(id_editorial_sello);


--
-- TOC entry 4966 (class 2606 OID 17050)
-- Name: producto fk_producto_rango; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.producto
    ADD CONSTRAINT fk_producto_rango FOREIGN KEY (id_rango_etario) REFERENCES public.rango_etario(id_rango_etario);


--
-- TOC entry 4967 (class 2606 OID 17040)
-- Name: producto fk_producto_tipo; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.producto
    ADD CONSTRAINT fk_producto_tipo FOREIGN KEY (id_tipo_producto) REFERENCES public.tipo_producto(id_tipo_producto);


--
-- TOC entry 4960 (class 2606 OID 17020)
-- Name: usuario fk_usuario_persona; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT fk_usuario_persona FOREIGN KEY (id_persona) REFERENCES public.persona(id_persona);


--
-- TOC entry 4961 (class 2606 OID 17015)
-- Name: usuario fk_usuario_rol; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT fk_usuario_rol FOREIGN KEY (id_rol) REFERENCES public.rol(id_rol);


--
-- TOC entry 4979 (class 2606 OID 17120)
-- Name: venta fk_venta_cliente; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.venta
    ADD CONSTRAINT fk_venta_cliente FOREIGN KEY (id_cliente) REFERENCES public.cliente(id_cliente);


--
-- TOC entry 4980 (class 2606 OID 17125)
-- Name: venta fk_venta_empleado; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.venta
    ADD CONSTRAINT fk_venta_empleado FOREIGN KEY (id_empleado) REFERENCES public.empleado(id_empleado);


--
-- TOC entry 4981 (class 2606 OID 17110)
-- Name: venta fk_venta_estado; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.venta
    ADD CONSTRAINT fk_venta_estado FOREIGN KEY (id_estado_venta) REFERENCES public.estado_venta(id_estado_venta);


--
-- TOC entry 4982 (class 2606 OID 17115)
-- Name: venta fk_venta_sucursal; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.venta
    ADD CONSTRAINT fk_venta_sucursal FOREIGN KEY (id_sucursal) REFERENCES public.sucursal(id_sucursal);


--
-- TOC entry 4983 (class 2606 OID 17150)
-- Name: venta_pago fk_vp_pago; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.venta_pago
    ADD CONSTRAINT fk_vp_pago FOREIGN KEY (id_forma_pago) REFERENCES public.forma_pago(id_forma_pago);


--
-- TOC entry 4984 (class 2606 OID 17145)
-- Name: venta_pago fk_vp_venta; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.venta_pago
    ADD CONSTRAINT fk_vp_venta FOREIGN KEY (id_venta) REFERENCES public.venta(id_venta);


-- Completed on 2026-06-20 22:19:57

--
-- PostgreSQL database dump complete
--


