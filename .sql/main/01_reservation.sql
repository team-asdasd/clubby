CREATE TABLE main.reservations
(
  reservationid SERIAL    NOT NULL,
  userid        INTEGER   NOT NULL,
  cottageid     INTEGER   NOT NULL,
  paymentid     INTEGER   NOT NULL,
  dateFrom      DATE      NOT NULL,
  dateTo        DATE      NOT NULL,
  created       TIMESTAMP NOT NULL,
  cancelled     BOOLEAN   NOT NULL DEFAULT FALSE,
  CONSTRAINT reservations_pkey PRIMARY KEY (reservationid),
  CONSTRAINT reservations_users_id_fk FOREIGN KEY (userid) REFERENCES main.users (id),
  CONSTRAINT reservations_cottages_id_fk FOREIGN KEY (cottageid) REFERENCES main.cottages (id),
  CONSTRAINT reservations_payments_paymentid_fk FOREIGN KEY (paymentid) REFERENCES payment.payments (paymentid)
);