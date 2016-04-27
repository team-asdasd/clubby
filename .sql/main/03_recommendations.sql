-- Table: main.recommendations

-- DROP TABLE main.recommendations;

CREATE TABLE main.recommendations
(
  id serial NOT NULL,
  user_from integer,
  user_to integer,
  recommendation_code text,
  CONSTRAINT pk_recommendations_id PRIMARY KEY (id),
  CONSTRAINT fk_recommendations_users1 FOREIGN KEY (user_from)
      REFERENCES main.users (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_recommendations_users2 FOREIGN KEY (user_from)
      REFERENCES main.users (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE main.recommendations
  OWNER TO adminu6davmr;