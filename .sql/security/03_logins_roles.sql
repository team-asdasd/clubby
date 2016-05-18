CREATE TABLE security.logins_roles (
  role_name TEXT,
  username TEXT,
  FOREIGN KEY (username) REFERENCES security.logins (username)
  MATCH SIMPLE ON UPDATE CASCADE ON DELETE CASCADE
);
