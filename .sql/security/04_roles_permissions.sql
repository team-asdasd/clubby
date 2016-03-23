-- Table: security.roles_permissions

-- DROP TABLE security.roles_permissions;

CREATE TABLE security.roles_permissions
(
  role_name text NOT NULL,
  permission text,
  CONSTRAINT roles_permissions_pkey PRIMARY KEY (role_name)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE security.roles_permissions
  OWNER TO adminumaaqhz;


-- Constraint: security.roles_permissions_pkey

-- ALTER TABLE security.roles_permissions DROP CONSTRAINT roles_permissions_pkey;

ALTER TABLE security.roles_permissions
  ADD CONSTRAINT roles_permissions_pkey PRIMARY KEY(role_name);
