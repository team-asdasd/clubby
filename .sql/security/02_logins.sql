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


-- Constraint: security.logins_pkey

-- ALTER TABLE security.logins DROP CONSTRAINT logins_pkey;

ALTER TABLE security.logins
  ADD CONSTRAINT logins_pkey PRIMARY KEY(id);


-- Constraint: security.logins_username_unique

-- ALTER TABLE security.logins DROP CONSTRAINT logins_username_unique;

ALTER TABLE security.logins
  ADD CONSTRAINT logins_username_unique UNIQUE(username);
