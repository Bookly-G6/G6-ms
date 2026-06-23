INSERT INTO public.rol (id_rol, nombre_rol)
VALUES
    (1, 'CLIENTE'),
    (2, 'ADMIN'),
    (3, 'VENDEDOR');

INSERT INTO public.persona (
    id_persona,
    nombre,
    apellido,
    dni,
    telefono
) VALUES
('6de695a9-34cf-4de3-a160-f6c150ec273e', 'Juan', 'Pérez', '30123456', '3704123456'),
('dd5343c3-fd01-4ff7-a851-0d64756da65f', 'María', 'Gómez', '28987654', '3704234567'),
('4d34de3e-be7b-4813-ad44-940ecab908ad', 'Carlos', 'López', '31567890', '3704345678'),
('e1615610-2fa4-43b6-9dfc-b10fe5fb80c5', 'Ana', 'Martínez', '29876543', '3704456789'),
('74521d09-d249-4a44-9f25-7cd57663e96c', 'Luis', 'Fernández', '32233445', '3704567890'),
('cc4be056-9fa3-4562-a85b-8486ea30561f', 'Sofía', 'Ramírez', '33445566', '3704678901'),
('05566392-d1ce-43f3-ba0f-db3ac615c194', 'Diego', 'Torres', '27654321', '3704789012'),
('d6823641-824d-4096-a5ca-5196ccbc4213', 'Lucía', 'Sánchez', '30999888', '3704890123'),
('384d9e4b-4efb-456c-ac71-9a15424d6619', 'Martín', 'Díaz', '34111222', '3704901234'),
('3406f143-0ab6-4821-98c9-de0c34b3aca7', 'Valentina', 'Ruiz', '35566777', '3704012345');

INSERT INTO public.usuario (
    id_usuario,
    id_persona,
    email,
    "password",
    id_rol,
    activo
) VALUES
('30e32ad0-c9f5-461f-91b8-a967600b7ba1', 'a1a1a1a1-a1a1-a1a1-a1a1-a1a1a1a1a1a1', 'carlos.logistico@empresa.com', '$2a$10$7EqJtq98hPqEX7fNZaFWoOHi6M9iS7G4rM3M5N6g6s0TQ2P4d5H6W', 3, true),
('9bb6c417-3ba9-46c3-8dba-7216930efe2f', 'c3c3c3c3-c3c3-c3c3-c3c3-c3c3c3c3c3c3', 'lucia.compras@empresa.com', '$2a$10$7EqJtq98hPqEX7fNZaFWoOHi6M9iS7G4rM3M5N6g6s0TQ2P4d5H6W', 1, true),
('b5e3850a-60c1-4c2a-916d-1961d087a01b', '6de695a9-34cf-4de3-a160-f6c150ec273e', 'juan.perez@gmail.com', '$2a$10$7EqJtq98hPqEX7fNZaFWoOHi6M9iS7G4rM3M5N6g6s0TQ2P4d5H6W', 2, true),
('da735e5c-36f3-42b3-9d84-cce471ef2ce2', 'dd5343c3-fd01-4ff7-a851-0d64756da65f', 'maria.gomez@gmail.com', '$2a$10$7EqJtq98hPqEX7fNZaFWoOHi6M9iS7G4rM3M5N6g6s0TQ2P4d5H6W', 1, true),
('4a492c77-f774-4fd1-b04f-cf900a517c92', '4d34de3e-be7b-4813-ad44-940ecab908ad', 'carlos.lopez@gmail.com', '$2a$10$7EqJtq98hPqEX7fNZaFWoOHi6M9iS7G4rM3M5N6g6s0TQ2P4d5H6W', 3, true),
('39af0735-9855-4156-89e9-ae6d8e7f1790', 'e1615610-2fa4-43b6-9dfc-b10fe5fb80c5', 'ana.martinez@gmail.com', '$2a$10$7EqJtq98hPqEX7fNZaFWoOHi6M9iS7G4rM3M5N6g6s0TQ2P4d5H6W', 1, true),
('3f0cdd74-55ec-4fc7-befb-8cdf479becfc', '74521d09-d249-4a44-9f25-7cd57663e96c', 'luis.fernandez@gmail.com', '$2a$10$7EqJtq98hPqEX7fNZaFWoOHi6M9iS7G4rM3M5N6g6s0TQ2P4d5H6W', 3, true),
('ccce25cd-16a2-4ac1-b9f3-e1f1ac28e7c9', 'cc4be056-9fa3-4562-a85b-8486ea30561f', 'sofia.ramirez@gmail.com', '$2a$10$7EqJtq98hPqEX7fNZaFWoOHi6M9iS7G4rM3M5N6g6s0TQ2P4d5H6W', 1, true),
('4948025b-0c2d-473b-bdd7-3c51d76a6de9', '05566392-d1ce-43f3-ba0f-db3ac615c194', 'diego.torres@gmail.com', '$2a$10$7EqJtq98hPqEX7fNZaFWoOHi6M9iS7G4rM3M5N6g6s0TQ2P4d5H6W', 3, true),
('bfb3f9a6-f2d2-4c92-8d28-4977307b1d55', 'd6823641-824d-4096-a5ca-5196ccbc4213', 'lucia.sanchez@gmail.com', '$2a$10$7EqJtq98hPqEX7fNZaFWoOHi6M9iS7G4rM3M5N6g6s0TQ2P4d5H6W', 1, true),
('2404a613-39b6-4704-ae63-b22c5a43d769', '384d9e4b-4efb-456c-ac71-9a15424d6619', 'martin.diaz@gmail.com', '$2a$10$7EqJtq98hPqEX7fNZaFWoOHi6M9iS7G4rM3M5N6g6s0TQ2P4d5H6W', 3, true),
('19a3daf6-1563-4498-b7a1-cd860b0d7b68', '3406f143-0ab6-4821-98c9-de0c34b3aca7', 'valentina.ruiz@gmail.com', '$2a$10$7EqJtq98hPqEX7fNZaFWoOHi6M9iS7G4rM3M5N6g6s0TQ2P4d5H6W', 1, true)
ON CONFLICT (email) DO NOTHING;

INSERT INTO public.producto (
    id_producto,
    codigo_barras,
    nombre_producto,
    descripcion,
    precio_costo,
    precio_actual,
    activo,
    id_tipo_producto,
    id_editorial_sello,
    id_rango_etario,
    atributos_especificos
) VALUES

('bb858810-7e62-4f7f-95be-514edb663acc','9780132350887','Clean Code','El libro definitivo para escribir código limpio.',12000.50,28500.00,true,1,2,6,'{"tapa":"blanda","idioma":"español","paginas":464}'),

('8987ee66-bb9c-4304-bbd1-873da1caa4e4','9780201616224','The Pragmatic Programmer','Clásico sobre buenas prácticas de programación.',15000.00,32000.00,true,1,2,6,'{"tapa":"blanda","idioma":"español","paginas":352}'),

('20f19791-dfc4-4f78-8e7c-2eba0dccdad8','9780131103627','The C Programming Language','Referencia esencial del lenguaje C.',18000.00,35000.00,true,1,2,6,'{"tapa":"dura","idioma":"inglés","paginas":274}'),

('871006db-ee90-4188-97c4-9ce61f21e884','9788497594257','El Hobbit','Aventura épica en la Tierra Media.',8000.00,19500.00,true,1,3,2,'{"tapa":"blanda","idioma":"español","paginas":310}'),

('64f4ba08-2309-48fc-b822-048cb635f37c','9789505470632','Cien Años de Soledad','Obra maestra de García Márquez.',9000.00,21000.00,true,1,3,6,'{"tapa":"blanda","idioma":"español","paginas":417}'),

('4bf05595-2442-4d50-abff-e0a930ed1882','9780439708180','Harry Potter y la Piedra Filosofal','Inicio de la saga mágica.',10000.00,25000.00,true,1,1,5,'{"tapa":"blanda","idioma":"español","paginas":223}'),

('acd1cb6b-3259-4690-8110-3a2edc6b99f2','9789877251234','El Principito','Clásico de la literatura universal.',5000.00,15000.00,true,1,1,3,'{"tapa":"blanda","idioma":"español","paginas":96}'),

('71ff57ed-a7da-42bf-bd62-29e05efb46e2','9789878000100','Ataque a los Titanes Vol. 1','Inicio del famoso manga.',4000.00,12000.00,true,6,6,5,'{"idioma":"español","formato":"tankobon","paginas":192}'),

('c5b34cf7-83d7-4daf-bef0-bd8b43c7b801','9789878000101','Naruto Vol. 1','Comienzo de la historia ninja.',4200.00,12500.00,true,6,6,5,'{"idioma":"español","formato":"tankobon","paginas":190}'),

('f1a58c3d-f04a-4af8-87c2-764eade43a09','9789878000102','One Piece Vol. 1','Aventura pirata épica.',4300.00,13000.00,true,6,6,5,'{"idioma":"español","formato":"tankobon","paginas":200}'),

('2ee96d0d-f1d2-484d-b4b1-29f55a6f58ca','9789878000103','Death Note Vol. 1','Thriller psicológico.',4500.00,13500.00,true,6,6,6,'{"idioma":"español","formato":"tankobon","paginas":200}'),

('26c9f30b-839f-4d93-b181-74939a512129','9789878000104','Demon Slayer Vol. 1','Historia de cazadores de demonios.',4600.00,14000.00,true,6,6,5,'{"idioma":"español","formato":"tankobon","paginas":192}'),

('1376133b-f59b-4cac-8bf5-062cdf863ad8','9781491950357','Designing Data-Intensive Applications','Arquitectura de sistemas modernos.',20000.00,40000.00,true,1,2,6,'{"tapa":"blanda","idioma":"inglés","paginas":616}'),

('0e8af7f4-5714-46a1-9bc9-47f36e14919c','9780134494166','Clean Architecture','Buenas prácticas de arquitectura.',17000.00,36000.00,true,1,2,6,'{"tapa":"blanda","idioma":"español","paginas":432}'),

('d02fe3f9-d8e9-4f7d-9b8b-5254881d3dcc','9780321125217','Domain-Driven Design','Diseño basado en dominio.',22000.00,45000.00,true,1,2,6,'{"tapa":"dura","idioma":"inglés","paginas":560}');

INSERT INTO public.forma_pago (nombre_pago)
VALUES 
('Efectivo'),
('Transferencia'),
('Tarjeta de crédito'),
('Tarjeta de débito');

INSERT INTO public.inventario (id_sucursal, id_producto, stock)
VALUES
(1, '7ce9f235-3215-4ab9-b3a0-a31e3d9b6a53', 100),
(1, 'bb858810-7e62-4f7f-95be-514edb663acc', 100),
(1, '8987ee66-bb9c-4304-bbd1-873da1caa4e4', 100),
(1, '20f19791-dfc4-4f78-8e7c-2eba0dccdad8', 100),
(1, '871006db-ee90-4188-97c4-9ce61f21e884', 100),
(1, '64f4ba08-2309-48fc-b822-048cb635f37c', 100),
(1, '4bf05595-2442-4d50-abff-e0a930ed1882', 100),
(1, 'acd1cb6b-3259-4690-8110-3a2edc6b99f2', 100),
(1, '71ff57ed-a7da-42bf-bd62-29e05efb46e2', 100),
(1, 'c5b34cf7-83d7-4daf-bef0-bd8b43c7b801', 100),
(1, 'f1a58c3d-f04a-4af8-87c2-764eade43a09', 100),
(1, '2ee96d0d-f1d2-484d-b4b1-29f55a6f58ca', 100),
(1, '26c9f30b-839f-4d93-b181-74939a512129', 100),
(1, '1376133b-f59b-4cac-8bf5-062cdf863ad8', 100),
(1, '0e8af7f4-5714-46a1-9bc9-47f36e14919c', 100),
(1, 'd02fe3f9-d8e9-4f7d-9b8b-5254881d3dcc', 100);

INSERT INTO public.promocion (nombre_promo, porcentaje_descuento, activa)
VALUES 
('Descuento 5%', 5.00, true),
('Descuento 10%', 10.00, true);

INSERT INTO public.empleado (id_empleado, id_persona, legajo, cargo, id_sucursal)
VALUES
('4c6d82bc-d690-4fc6-a3c7-8fa6e678f783', '74521d09-d249-4a44-9f25-7cd57663e96c', 'LEG-001', 'vendedor', 1),
('d5206ef6-ec3d-436c-8f84-c05eab7c1745', '05566392-d1ce-43f3-ba0f-db3ac615c194', 'LEG-002', 'vendedor', 1),
('f4aa3472-c203-4596-9541-fbf46842d632', '384d9e4b-4efb-456c-ac71-9a15424d6619', 'LEG-003', 'vendedor', 1);




