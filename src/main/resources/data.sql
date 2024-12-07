-- Insertar doctores
INSERT INTO doctors (first_name, last_name, maternal_last_name, specialty) 
VALUES 
('Juan', 'Pérez', 'García', 'Medicina Interna'),
('María', 'López', 'Rodríguez', 'Medicina Interna'),
('Carlos', 'González', 'Martínez', 'Medicina Interna')
ON DUPLICATE KEY UPDATE specialty = specialty;

-- Insertar consultorios
INSERT INTO consulting_rooms (room_number, floor) 
VALUES 
(101, 1),
(102, 1),
(201, 2)
ON DUPLICATE KEY UPDATE floor = floor;