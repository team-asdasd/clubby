CREATE TABLE payment.payments
(
  paymentId SERIAL NOT NULL,
  paymentTypeId INT  NOT NULL,
  amount INT  NOT NULL,
  paytext TEXT  NOT NULL,
  paymentSettingsId INT NOT NULL,
  active BOOLEAN DEFAULT TRUE,
  CONSTRAINT payments_pkey PRIMARY KEY (paymentId),
  CONSTRAINT fk_payments_payments_settings FOREIGN KEY (paymentSettingsId)
  REFERENCES payment.paymentsSettings (paymentSettingsId) MATCH SIMPLE,
  CONSTRAINT fk_payments_payment_types FOREIGN KEY (paymentTypeId)
  REFERENCES payment.paymentTypes (paymentTypeId) MATCH SIMPLE,
  CONSTRAINT unique_key UNIQUE (paymentTypeId)

)
WITH (OIDS =FALSE);