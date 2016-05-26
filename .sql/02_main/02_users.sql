-- Table: main.users

-- DROP TABLE main.users;

CREATE TABLE main.users
(
  id         SERIAL NOT NULL,
  name       TEXT,
  login      INTEGER,
  picture 	 TEXT,
  CONSTRAINT pk_users_id PRIMARY KEY (id),
  CONSTRAINT fk_users_logins FOREIGN KEY (login)
  REFERENCES security.logins (id) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION)
WITH (OIDS =FALSE);