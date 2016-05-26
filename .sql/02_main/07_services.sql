CREATE TABLE main.services
(
    id SERIAL PRIMARY KEY NOT NULL,
    cottage_id INT NOT NULL,
    price INTEGER NOT NULL,
    description TEXT,
	max_count INTEGER,
    CONSTRAINT Service_cottages_id_fk FOREIGN KEY (cottage_id) REFERENCES main.cottages (id) ON DELETE CASCADE ON UPDATE CASCADE
);