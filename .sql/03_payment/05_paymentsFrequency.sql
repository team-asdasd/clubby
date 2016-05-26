CREATE TABLE payment.paymentsFrequency
(
  frequencyId INT NOT NULL,
  name TEXT  NOT NULL,
  CONSTRAINT payments_frequency_pkey PRIMARY KEY (frequencyId),
  CONSTRAINT payments_frequency_unique_name UNIQUE (name)
)
WITH (OIDS =FALSE);