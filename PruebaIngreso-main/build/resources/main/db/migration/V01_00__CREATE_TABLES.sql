CREATE TABLE IF NOT EXISTS subasta_item
(
  item_id     INT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
  item_nombre VARCHAR(255)                     NOT NULL
);

CREATE TABLE IF NOT EXISTS subasta_usuario
(
  usuario_id     INT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
  usuario_nombre VARCHAR(255)                     NOT NULL
);

CREATE TABLE IF NOT EXISTS subasta_apuesta
(
  apuesta_id         INT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
  apuesta_usuario_id INT                              NOT NULL REFERENCES subasta_usuario (usuario_id),
  apuesta_item_id    INT                              NOT NULL REFERENCES subasta_item (item_id),
  apuesta_monto      INT                              NOT NULL
);

