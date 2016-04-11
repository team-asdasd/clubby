-- Table: security.logins

-- DROP TABLE security.logins;

CREATE TABLE security.logins
(
  id       SERIAL NOT NULL,
  username TEXT,
  password TEXT,
  CONSTRAINT logins_pkey PRIMARY KEY (id),
  CONSTRAINT logins_username_unique UNIQUE (username)
)
WITH (OIDS =FALSE);
