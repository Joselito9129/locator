-- =========================
-- LIMPIEZA
-- =========================
DELETE FROM despacho_proceso_detalle;
DELETE FROM despacho_proceso;
DELETE FROM tienda;
DELETE FROM ventana_salida;
DELETE FROM plan_carga_camion;
DELETE FROM plan_carga_camion_tienda;

-- =========================
-- VENTANA
-- =========================
INSERT INTO ventana_salida (id, hora_salida, estado, usuario_creacion, fecha_creacion)
VALUES (1, '08:00:00', 'ACTIVO', 'demo', NOW());

-- =========================
-- TIENDAS (3 RUTAS)
-- =========================
INSERT INTO tienda
(id, codigo_tienda, nombre_tienda, direccion, latitud, longitud, usuario_creacion, fecha_creacion, recibe_tonelaje, ruta)
VALUES
    (1, 'T001', 'R1 T1 MANY ORDERS', 'DIR1',0,0,'demo',NOW(),'5,10',1),
    (2, 'T002', 'R1 T2 FLEX', 'DIR2',0,0,'demo',NOW(),'5,10,22',1),
    (3, 'T003', 'R1 T3 SOLO 22', 'DIR3',0,0,'demo',NOW(),'22',1),
    (9, 'T009', 'R1 T9 MEDIA', 'DIR9',0,0,'demo',NOW(),'10,22',1),
    (10,'T010','R1 T10 SOLO 22 SMALL','DIR10',0,0,'demo',NOW(),'22',1),
    (4, 'T004', 'R2 T4 MEDIA', 'DIR4',0,0,'demo',NOW(),'10,22',2),
    (5, 'T005', 'R2 T5 MIX', 'DIR5',0,0,'demo',NOW(),'5,10',2),
    (6, 'T006', 'R2 T6 SOLO 5', 'DIR6',0,0,'demo',NOW(),'5',2),
    (7, 'T007', 'R2 T7 FLEX', 'DIR7',0,0,'demo',NOW(),'5,10,22',2),
    (8, 'T008', 'R2 T8 SOLO 10', 'DIR8',0,0,'demo',NOW(),'10',2),
    (11,'T011','R3 T11 GRANDE','DIR11',0,0,'demo',NOW(),'5,10,22',3),
    (12,'T012','R3 T12 VOLUMEN','DIR12',0,0,'demo',NOW(),'5,10,22',3);

-- =========================
-- DESPACHOS
-- =========================
INSERT INTO despacho_proceso (id, tienda_id, estado, usuario_creacion, fecha_creacion)
VALUES
    (1,1,'EN_PROCESO','demo',NOW()),
    (2,2,'EN_PROCESO','demo',NOW()),
    (3,3,'EN_PROCESO','demo',NOW()),
    (4,4,'EN_PROCESO','demo',NOW()),
    (5,5,'EN_PROCESO','demo',NOW()),
    (6,6,'EN_PROCESO','demo',NOW()),
    (7,7,'EN_PROCESO','demo',NOW()),
    (8,8,'EN_PROCESO','demo',NOW()),
    (9,9,'EN_PROCESO','demo',NOW()),
    (10,10,'EN_PROCESO','demo',NOW()),
    (11,11,'EN_PROCESO','demo',NOW()),
    (12,12,'EN_PROCESO','demo',NOW());

-- =========================
-- DETALLE (CASOS REALES)
-- =========================

-- =========================
-- RUTA 1
-- =========================

-- T1 → 6 pedidos (mezcla)
INSERT INTO despacho_proceso_detalle VALUES
                                         (1,1,'A1',50,20,0.05,'ACTIVO','demo',NOW()),
                                         (2,1,'A2',50,20,0.05,'ACTIVO','demo',NOW()),
                                         (3,1,'A3',50,20,0.05,'ACTIVO','demo',NOW()),
                                         (4,1,'A4',50,20,0.05,'ACTIVO','demo',NOW()),
                                         (5,1,'A5',50,20,0.05,'ACTIVO','demo',NOW()),
                                         (6,1,'A6',50,20,0.05,'ACTIVO','demo',NOW()),
                                         (7,2,'B1',200,10,0.05,'ACTIVO','demo',NOW()),
                                         (8,2,'B2',200,10,0.05,'ACTIVO','demo',NOW()),
                                         (9,2,'B3',200,10,0.05,'ACTIVO','demo',NOW()),
                                         (10,2,'B4',200,10,0.05,'ACTIVO','demo',NOW()),
                                         (11,3,'C1',2000,10,0.02,'ACTIVO','demo',NOW()),
                                         (12,3,'C2',1500,10,0.02,'ACTIVO','demo',NOW()),
                                         (13,9,'D1',900,10,0.04,'ACTIVO','demo',NOW()),
                                         (14,9,'D2',400,10,0.04,'ACTIVO','demo',NOW()),
                                         (15,10,'E1',50,10,0.02,'ACTIVO','demo',NOW()),
                                         (16,4,'F1',800,10,0.05,'ACTIVO','demo',NOW()),
                                         (17,5,'G1',300,10,0.05,'ACTIVO','demo',NOW()),
                                         (18,5,'G2',300,10,0.05,'ACTIVO','demo',NOW()),
                                         (19,6,'H1',1000,10,0.05,'ACTIVO','demo',NOW()),
                                         (20,6,'H2',800,10,0.05,'ACTIVO','demo',NOW()),
                                         (21,6,'H3',700,10,0.05,'ACTIVO','demo',NOW()),
                                         (22,6,'H4',600,10,0.05,'ACTIVO','demo',NOW()),
                                         (23,7,'I1',200,10,0.05,'ACTIVO','demo',NOW()),
                                         (24,7,'I2',200,10,0.05,'ACTIVO','demo',NOW()),
                                         (25,8,'J1',600,10,0.05,'ACTIVO','demo',NOW()),
                                         (26,8,'J2',500,10,0.05,'ACTIVO','demo',NOW()),
                                         (27,8,'J3',400,10,0.05,'ACTIVO','demo',NOW()),
                                         (28,8,'J4',300,10,0.05,'ACTIVO','demo',NOW()),
                                         (29,8,'J5',200,10,0.05,'ACTIVO','demo',NOW()),
                                         (30,11,'K1',3000,10,0.02,'ACTIVO','demo',NOW()),
                                         (31,11,'K2',3000,10,0.02,'ACTIVO','demo',NOW()),
                                         (32,12,'L1',200,5,0.50,'ACTIVO','demo',NOW()),
                                         (33,12,'L2',200,5,0.50,'ACTIVO','demo',NOW());