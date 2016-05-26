-- Table: security.roles_permissions

-- DROP TABLE security.roles_permissions;

CREATE TABLE security.roles_permissions
(
  role_name  TEXT NOT NULL,
  permission TEXT,
  CONSTRAINT roles_permissions_pkey PRIMARY KEY (role_name)
)
WITH (OIDS =FALSE);