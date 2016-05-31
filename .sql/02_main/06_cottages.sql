CREATE TABLE main.cottages
(
    id SERIAL PRIMARY KEY NOT NULL,
    title TEXT,
    bedcount INTEGER,
    imageurl TEXT,
    version INTEGER,
	price INTEGER NOT NULL,
	availablefrom DATE,
	availableto DATE,
	description TEXT
);