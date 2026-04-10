CREATE TABLE tienda (
                        id BIGINT PRIMARY KEY,
                        codigo_tienda VARCHAR(50),
                        nombre_tienda VARCHAR(100),
                        direccion VARCHAR(255),
                        latitud DECIMAL(10,6),
                        longitud DECIMAL(10,6),
                        usuario_creacion VARCHAR(100),
                        fecha_creacion TIMESTAMP,
                        recibe_tonelaje VARCHAR(150),
                        ruta INT
);

CREATE TABLE centro_distribucion (
                                     id BIGINT PRIMARY KEY,
                                     codigo_centro VARCHAR(50),
                                     nombre_centro VARCHAR(100),
                                     direccion VARCHAR(255),
                                     latitud DECIMAL(10,6),
                                     longitud DECIMAL(10,6),
                                     usuario_creacion VARCHAR(100),
                                     fecha_creacion TIMESTAMP
);

CREATE TABLE camion (
                        id BIGINT PRIMARY KEY,
                        codigo_camion VARCHAR(50),
                        placa VARCHAR(20),
                        descripcion VARCHAR(100),
                        peso_maximo_kg DECIMAL(10,2),
                        volumen_maximo_m3 DECIMAL(10,2),
                        tipo_camion VARCHAR(20),
                        estado VARCHAR(20),
                        usuario_creacion VARCHAR(100),
                        fecha_creacion TIMESTAMP
);

CREATE TABLE pedido (
                        id BIGINT PRIMARY KEY,
                        tienda_id BIGINT,
                        estado VARCHAR(20),
                        usuario_creacion VARCHAR(100),
                        fecha_creacion TIMESTAMP,
                        FOREIGN KEY (tienda_id) REFERENCES tienda(id)
);

CREATE TABLE pedido_detalle (
                                id BIGINT PRIMARY KEY,
                                pedido_id BIGINT,
                                producto VARCHAR(100),
                                cantidad INT,
                                peso_unitario_kg DECIMAL(10,2),
                                volumen_unitario_m3 DECIMAL(10,3),
                                estado VARCHAR(20),
                                usuario_creacion VARCHAR(100),
                                fecha_creacion TIMESTAMP,
                                FOREIGN KEY (pedido_id) REFERENCES pedido(id)
);

CREATE TABLE despacho_proceso (
                                  id BIGINT PRIMARY KEY,
                                  tienda_id BIGINT,
                                  estado VARCHAR(50),
                                  usuario_creacion VARCHAR(100),
                                  fecha_creacion TIMESTAMP
);

CREATE TABLE despacho_proceso_detalle (
                                          id BIGINT PRIMARY KEY,
                                          despacho_id BIGINT,
                                          producto VARCHAR(100),
                                          cantidad INT,
                                          peso_unitario_kg DECIMAL(10,2),
                                          volumen_unitario_m3 DECIMAL(10,3),
                                          estado VARCHAR(20),
                                          usuario_creacion VARCHAR(100),
                                          fecha_creacion TIMESTAMP,
                                          FOREIGN KEY (despacho_id) REFERENCES despacho_proceso(id)
);

CREATE TABLE ventana_salida (
                                id BIGINT PRIMARY KEY,
                                hora_salida TIME,
                                estado VARCHAR(20),
                                usuario_creacion VARCHAR(100),
                                fecha_creacion TIMESTAMP
);

CREATE TABLE plan_carga (
                            id_plan BIGINT PRIMARY KEY,
                            estado VARCHAR(255),
                            fecha DATE,
                            fecha_actualizacion DATETIME(6),
                            fecha_creacion DATETIME(6),
                            usuario_actualizacion VARCHAR(255),
                            usuario_creacion VARCHAR(255),
                            ventana_id BIGINT,
                            FOREIGN KEY (ventana_id) REFERENCES ventana_salida(id)
);

CREATE TABLE plan_carga_camion (
                                   id_plan_camion BIGINT PRIMARY KEY,
                                   capacidad_peso DECIMAL(38,2),
                                   capacidad_volumen DECIMAL(38,2),
                                   estado VARCHAR(255),
                                   fecha_actualizacion DATETIME(6),
                                   fecha_creacion DATETIME(6),
                                   peso_actual DECIMAL(38,2),
                                   plan_id BIGINT,
                                   porcentaje_ocupacion DECIMAL(38,2),
                                   tipo_camion VARCHAR(255),
                                   usuario_actualizacion VARCHAR(255),
                                   usuario_creacion VARCHAR(255),
                                   volumen_actual DECIMAL(38,2),
                                   ruta INT,
                                   FOREIGN KEY (plan_id) REFERENCES plan_carga(id_plan)
);

CREATE TABLE plan_carga_camion_tienda (
                                          id_plan_camion_tienda BIGINT PRIMARY KEY,
                                          estado VARCHAR(255),
                                          fecha_actualizacion DATETIME(6),
                                          fecha_creacion DATETIME(6),
                                          peso_asignado DECIMAL(38,2),
                                          plan_camion_id BIGINT,
                                          tienda_id BIGINT,
                                          usuario_actualizacion VARCHAR(255),
                                          usuario_creacion VARCHAR(255),
                                          volumen_asignado DECIMAL(38,2),
                                          ruta BIGINT,
                                          FOREIGN KEY (plan_camion_id) REFERENCES plan_carga_camion(id_plan_camion),
                                          FOREIGN KEY (tienda_id) REFERENCES tienda(id)
);

CREATE TABLE ruta_tienda (
                             id_ruta BIGINT PRIMARY KEY,
                             id_centro BIGINT,
                             estado VARCHAR(20),
                             fecha_actualizacion DATETIME(6),
                             fecha_creacion DATETIME(6),
                             nombre_ruta VARCHAR(20),
                             id_tienda BIGINT,
                             usuario_actualizacion VARCHAR(100),
                             usuario_creacion VARCHAR(100),
                             FOREIGN KEY (id_tienda) REFERENCES tienda(id),
                             FOREIGN KEY (id_centro) REFERENCES centro_distribucion(id)
);

CREATE TABLE ruta_tienda_punto (
                                   id_ruta_punto BIGINT PRIMARY KEY,
                                   estado VARCHAR(20),
                                   fecha_actualizacion DATETIME(6),
                                   fecha_creacion DATETIME(6),
                                   latitud DOUBLE,
                                   longitud DOUBLE,
                                   orden_punto INT,
                                   id_ruta BIGINT,
                                   tipo_punto VARCHAR(255),
                                   usuario_actualizacion VARCHAR(100),
                                   usuario_creacion VARCHAR(100),
                                   FOREIGN KEY (id_ruta) REFERENCES ruta_tienda(id_ruta)
);

CREATE TABLE rutas_geolocalizacion (
                                       id BIGINT PRIMARY KEY,
                                       estado VARCHAR(1),
                                       fecha_creacion DATETIME(6),
                                       guia VARCHAR(50),
                                       latitud_destino DECIMAL(10,7),
                                       latitud_origen DECIMAL(10,7),
                                       longitud_destino DECIMAL(10,7),
                                       longitud_origen DECIMAL(10,7),
                                       usuario_creacion VARCHAR(100)
);