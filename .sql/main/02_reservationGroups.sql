CREATE TABLE main.reservationGroups
(
  reservationGroupId SERIAL NOT NULL,
  userid INTEGER NOT NULL,
  generation INTEGER NOT NULL DEFAULT MAX(generation),
  groupNumber INTEGER NOT NULL,
  CONSTRAINT reservationGroups_pkey PRIMARY KEY (reservationGroupId),
  unique (userid, generation)
);
