-- Script para inicializar usuario ADMIN
-- Ejecutar este script después de crear la estructura de la BD

-- 1. Insertar rol ADMIN si no existe
INSERT INTO rol (id_rol, nombre_rol) 
VALUES (1, 'ADMIN')
ON CONFLICT DO NOTHING;

-- 2. Insertar rol CLIENTE si no existe
INSERT INTO rol (id_rol, nombre_rol)
VALUES (2, 'CLIENTE')
ON CONFLICT DO NOTHING;

-- 3. Insertar persona para el admin
INSERT INTO persona (id_persona, nombre, apellido, dni, telefono)
VALUES ('f47ac10b-58cc-4372-a567-0e02b2c3d479', 'Admin', 'System', '00000000', '1100000000')
ON CONFLICT DO NOTHING;

-- 4. Insertar usuario ADMIN
-- Email: admin@bookly.com
-- Password: 123456 (encriptada con BCrypt)
-- Password BCrypt hash para "123456": $2a$10$slYQmyNdGzin7olVN3p5Be7DlH.PKZbv5H8KnzzVgXXbVxzy2ND5C
INSERT INTO usuario (id_usuario, id_persona, email, password, id_rol, activo)
VALUES ('e47ac10b-58cc-4372-a567-0e02b2c3d479', 'f47ac10b-58cc-4372-a567-0e02b2c3d479', 'admin@bookly.com', '$2a$10$slYQmyNdGzin7olVN3p5Be7DlH.PKZbv5H8KnzzVgXXbVxzy2ND5C', 1, true)
ON CONFLICT DO NOTHING;
