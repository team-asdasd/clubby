CREATE TABLE payment.transactionTypes
(
  transactionTypeId SERIAL NOT NULL,
  name TEXT NOT NULL,
  CONSTRAINT moneyTransactions_pkey PRIMARY KEY (transactionTypeId)
)
WITH (OIDS =FALSE);