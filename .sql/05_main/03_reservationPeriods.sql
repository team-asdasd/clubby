CREATE TABLE main.reservationsPeriods
(
  reservationsPeriodId SERIAL NOT NULL,
  fromDate             DATE   NOT NULL,
  toDate               DATE   NOT NULL,
  CONSTRAINT reservationsPeriods_pkey PRIMARY KEY (reservationsPeriodId)
);
