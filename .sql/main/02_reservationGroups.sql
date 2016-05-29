CREATE TABLE main.reservationGroups
(
  reservationGroupId SERIAL NOT NULL,
  userid INTEGER NOT NULL,
  generation INTEGER NOT NULL DEFAULT MAX(generation),
  groupNumber INTEGER NOT NULL,
  CONSTRAINT reservationGroups_pkey PRIMARY KEY (reservationGroupId),
    CONSTRAINT fk_reservationGroups_users FOREIGN KEY (userId)
  REFERENCES main.users (id) MATCH SIMPLE,
  unique (userid, generation)
);
