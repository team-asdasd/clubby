CREATE TABLE main.services
(
    id SERIAL PRIMARY KEY NOT NULL,
    cottage_id INT NOT NULL,
    price INTEGER NOT NULL,
    description TEXT,
    CONSTRAINT Service_cottages_id_fk FOREIGN KEY (cottage_id) REFERENCES cottages (id) ON DELETE CASCADE ON UPDATE CASCADE
);