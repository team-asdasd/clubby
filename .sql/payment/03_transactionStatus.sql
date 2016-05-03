CREATE TABLE payment.transactionStatus
(
  status INT NOT NULL,
  name TEXT NOT NULL,
  CONSTRAINT moneyTransactions_pkey PRIMARY KEY (status)
)
WITH (OIDS =FALSE);