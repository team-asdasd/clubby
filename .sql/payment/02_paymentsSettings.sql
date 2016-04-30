CREATE TABLE payment.paymentsSettings
(
  paymentSettingsId SERIAL NOT NULL,
  projectid TEXT  NOT NULL,
  version TEXT  NOT NULL,
  currency TEXT  NOT NULL,
  CONSTRAINT paymentsSettings_pkey PRIMARY KEY (paymentSettingsId)
)
WITH (OIDS =FALSE);