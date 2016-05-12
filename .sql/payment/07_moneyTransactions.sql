CREATE TABLE payment.moneyTransactions
(
  transactionId TEXT NOT NULL,
  transactionTypeId INT NOT NULL,
  userId INT NOT NULL,
  status INT NOT NULL,
  creationTime TIMESTAMP NOT NULL,
  paymentId INT NOT NULL,
  CONSTRAINT moneyTransactions_pkey PRIMARY KEY (transactionId),
  CONSTRAINT fk_money_transactions_transaction_types FOREIGN KEY (transactionTypeId)
  REFERENCES payment.transactionTypes (transactionTypeId) MATCH SIMPLE,
  CONSTRAINT fk_money_transactions_users FOREIGN KEY (userId)
  REFERENCES main.users (id) MATCH SIMPLE,
  CONSTRAINT fk_money_transactions_payments FOREIGN KEY (paymentId)
  REFERENCES payment.payments (paymentId) MATCH SIMPLE,
  CONSTRAINT fk_money_transactions_status FOREIGN KEY (status)
  REFERENCES payment.transactionStatus (status) MATCH SIMPLE

)
WITH (OIDS =FALSE);