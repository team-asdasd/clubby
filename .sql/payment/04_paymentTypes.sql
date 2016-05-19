CREATE TABLE payment.paymentTypes
(
  paymentTypeId SERIAL NOT NULL,
  name TEXT  NOT NULL,
  description TEXT,
  CONSTRAINT paymentTypes_pkey PRIMARY KEY (paymentTypeId),
  CONSTRAINT unique_name UNIQUE (name)
)
WITH (OIDS =FALSE);