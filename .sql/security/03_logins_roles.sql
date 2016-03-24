-- Table: security.logins_roles

-- DROP TABLE security.logins_roles;

CREATE TABLE security.logins_roles
(
  role_name text,
  username text
)
WITH (
  OIDS=FALSE
);
ALTER TABLE security.logins_roles
  OWNER TO adminumaaqhz;
