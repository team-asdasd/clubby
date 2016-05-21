CREATE TABLE payment.transactionStatus
(
  status INT NOT NULL,
  name TEXT NOT NULL,
  CONSTRAINT transactionStatus_pkey PRIMARY KEY (status)
)
WITH (OIDS =FALSE);