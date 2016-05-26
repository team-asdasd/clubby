CREATE TABLE payment.pendingPayments
(
  pendingPaymentId SERIAL NOT NULL,
  userId INT NOT NULL,
  paymentId INT NOT NULL,
  CONSTRAINT pending_payments_pkey PRIMARY KEY (pendingPaymentId),
  CONSTRAINT fk_pending_payments_users FOREIGN KEY (userId)
  REFERENCES main.users (id) MATCH SIMPLE,
  CONSTRAINT fk_pending_payments_payments FOREIGN KEY (paymentId)
  REFERENCES payment.payments (paymentId) MATCH SIMPLE
)
WITH (OIDS =FALSE);