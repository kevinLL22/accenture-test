CREATE TABLE demo_message (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    text VARCHAR(100) NOT NULL
);

INSERT INTO demo_message (text) VALUES ('¡Conexión OK!');