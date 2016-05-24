CREATE TABLE main.form_fields
(
    id SERIAL PRIMARY KEY,
    name TEXT,
    type TEXT,
    validation_regex TEXT,
    required BOOLEAN DEFAULT FALSE ,
    visible BOOLEAN,
    description TEXT
);
CREATE UNIQUE INDEX formfields_name_uindex ON main.form_fields (name);