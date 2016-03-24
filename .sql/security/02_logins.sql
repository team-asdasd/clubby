-- Table: security.logins

-- DROP TABLE security.logins;

CREATE TABLE security.logins
(
  id serial NOT NULL,
  username text,
  password text,
  CONSTRAINT logins_pkey PRIMARY KEY (id),
  CONSTRAINT logins_username_unique UNIQUE (username)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE security.logins
  OWNER TO adminumaaqhz;
