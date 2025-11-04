-- INSERST INICIALES PARA LA BASE DE DATOS DE CAFETERÍA SOMA

-- ROLES INICIALES
INSERT INTO rol (nombre, descripcion, activo, fecha_creacion)
SELECT 'ADMIN', 'Acceso completo al sistema', true, NOW()
WHERE NOT EXISTS (
    SELECT 1 FROM rol WHERE nombre = 'ADMIN'
);

INSERT INTO rol (nombre, descripcion, activo, fecha_creacion)
SELECT 'CLIENTE', 'Acceso limitado, puede comprar y dejar reseñas', true, NOW()
WHERE NOT EXISTS (
    SELECT 1 FROM rol WHERE nombre = 'CLIENTE'
);


-- -- USUARIO ADMINISTRADOR
-- INSERT INTO usuario (nombre, correo, contrasena, telefono, id_rol, activo, fecha_creacion)
-- SELECT 'Administrador', 'admin@soma.com', '$2a$10$uROee.7N02ONn4QK3/NfC.MRrAWOc.BML0UpzEx7fUjH6Z5yTZFwO', '123456789',
--     (SELECT id_rol FROM rol WHERE nombre = 'ADMIN'), true, NOW()
-- WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE correo = 'admin@soma.com');
 
-- -- USUARIO CLIENTE (Contraseña: cliente123)
-- INSERT INTO usuario (nombre, correo, contrasena, telefono, id_rol, activo, fecha_creacion)
-- SELECT 'Cliente', 'cliente@soma.com', '$2a$10$e5zUFYYsCkT7EStVqjS9xOUD8s2.NNg97FqRSHr6emUNZfC5ffb4O', '987654321',
--     (SELECT id_rol FROM rol WHERE nombre = 'CLIENTE'), true, NOW()
-- WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE correo = 'cliente@soma.com');


-- CATEGORÍAS INICIALES
INSERT INTO categoria (nombre, descripcion, activo, fecha_creacion)
SELECT 'Cafés', 'Cafés calientes y fríos', true, NOW()
WHERE NOT EXISTS (
    SELECT 1 FROM categoria WHERE nombre = 'Cafés'
);

INSERT INTO categoria (nombre, descripcion, activo, fecha_creacion)
SELECT 'Postres', 'Tortas, muffins y repostería', true, NOW()
WHERE NOT EXISTS (
    SELECT 1 FROM categoria WHERE nombre = 'Postres'
);


-- PRODUCTOS INICIALES
INSERT INTO producto (nombre, descripcion, precio, stock, imagen_url, id_categoria, activo, fecha_creacion)
SELECT 
    'Latte', 
    'Café con leche espumada', 
    10.50, 
    25, 
    'https://example.com/img/latte.jpg',
    (SELECT id_categoria FROM categoria WHERE nombre = 'Cafés'),
    true,
    NOW()
WHERE NOT EXISTS (
    SELECT 1 FROM producto WHERE nombre = 'Latte'
);

INSERT INTO producto (nombre, descripcion, precio, stock, imagen_url, id_categoria, activo, fecha_creacion)
SELECT 
    'Capuccino', 
    'Café con leche y espuma de leche', 
    9.80, 
    30, 
    'https://example.com/img/capuccino.jpg',
    (SELECT id_categoria FROM categoria WHERE nombre = 'Cafés'),
    true,
    NOW()
WHERE NOT EXISTS (
    SELECT 1 FROM producto WHERE nombre = 'Capuccino'
);

INSERT INTO producto (nombre, descripcion, precio, stock, imagen_url, id_categoria, activo, fecha_creacion)
SELECT 
    'Cheesecake', 
    'Pastel de queso con frutos rojos', 
    15.00, 
    10, 
    'https://example.com/img/cheesecake.jpg',
    (SELECT id_categoria FROM categoria WHERE nombre = 'Postres'),
    true,
    NOW()
WHERE NOT EXISTS (
    SELECT 1 FROM producto WHERE nombre = 'Cheesecake'
);
