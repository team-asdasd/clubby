CREATE TABLE main.form_fields
(
  id               SERIAL PRIMARY KEY,
  name             TEXT,
  type             TEXT,
  validation_regex TEXT,
  required         BOOLEAN DEFAULT FALSE,
  visible          BOOLEAN,
  description      TEXT
);
CREATE UNIQUE INDEX formfields_name_uindex ON main.form_fields (name);

INSERT INTO main.form_fields (name, type, validation_regex, required, visible, description)
VALUES ('phoneNumber', 'text', NULL, TRUE, TRUE, 'Phone number');
INSERT INTO main.form_fields (name, type, validation_regex, required, visible, description)
VALUES ('about', 'text', NULL, TRUE, TRUE, 'About');
INSERT INTO main.form_fields (name, type, validation_regex, required, visible, description)
VALUES ('birthDate', 'date', NULL, TRUE, TRUE, 'Birth date');