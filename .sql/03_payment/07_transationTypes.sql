CREATE TABLE payment.transactionTypes
(
  transactionTypeId SERIAL NOT NULL,
  name TEXT NOT NULL,
  CONSTRAINT transactionTypes_pkey PRIMARY KEY (transactionTypeId)
)
WITH (OIDS =FALSE);
