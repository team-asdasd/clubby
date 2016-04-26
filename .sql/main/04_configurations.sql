-- Table: main.configurations

-- DROP TABLE main.configurations;

CREATE TABLE main.configurations
(
  id serial NOT NULL,
  key text,
  value text,
  CONSTRAINT pk_configurations PRIMARY KEY (id),
  CONSTRAINT unique_key UNIQUE (key)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE main.configurations
  OWNER TO adminu6davmr;
  
INSERT INTO main.configurations(key, value) VALUES ('min_recommendation_required', '2');
INSERT INTO main.configurations(key, value) VALUES ('max_recommendation_request', '5');
