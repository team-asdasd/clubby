CREATE TABLE main.configuration (
  key TEXT PRIMARY KEY NOT NULL,
  value TEXT
);
  
INSERT INTO main.configurations(key, value) VALUES ('min_recommendation_required', '2');
INSERT INTO main.configurations(key, value) VALUES ('max_recommendation_request', '5');
