CREATE TABLE payment.paymentsFrequency
(
  freqencyId INT NOT NULL,
  name TEXT  NOT NULL,
  CONSTRAINT payments_frequency_pkey PRIMARY KEY (freqencyId),
  CONSTRAINT payments_frequency_unique_name UNIQUE (name)
)
WITH (OIDS =FALSE);