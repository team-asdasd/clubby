
/* insert transaction status */
INSERT INTO payment.transactionstatus
SELECT * FROM (SELECT 1, 'pending') a
WHERE NOT EXISTS (
SELECT * FROM payment.transactionstatus
WHERE status = 1 AND name = 'pending');

INSERT INTO payment.transactionstatus
SELECT * FROM (SELECT 2, 'cancelled') a
WHERE NOT EXISTS (
SELECT * FROM payment.transactionstatus
WHERE status = 2 AND name = 'cancelled');

INSERT INTO payment.transactionstatus
SELECT * FROM (SELECT 3, 'failed') a
WHERE NOT EXISTS (
SELECT * FROM payment.transactionstatus
WHERE status = 3 AND name = 'failed');

INSERT INTO payment.transactionstatus
SELECT * FROM (SELECT 4, 'approved') a
WHERE NOT EXISTS (
SELECT * FROM payment.transactionstatus
WHERE status = 4 AND name = 'approved');

/* insert frequency*/
INSERT INTO payment.paymentsFrequency
SELECT * FROM (SELECT 0, 'any') a
WHERE NOT EXISTS (
SELECT * FROM payment.paymentsFrequency
WHERE frequencyId = 0 AND name = 'any');

INSERT INTO payment.paymentsFrequency
SELECT * FROM (SELECT 1, 'monthly') a
WHERE NOT EXISTS (
SELECT * FROM payment.paymentsFrequency
WHERE frequencyId = 1 AND name = 'monthly');

INSERT INTO payment.paymentsFrequency
SELECT * FROM (SELECT 2, 'yearly') a
WHERE NOT EXISTS (
SELECT * FROM payment.paymentsFrequency
WHERE frequencyId = 2 AND name = 'yearly');

INSERT INTO payment.paymentsFrequency
SELECT * FROM (SELECT 3, 'once') a
WHERE NOT EXISTS (
SELECT * FROM payment.paymentsFrequency
WHERE frequencyId = 3 AND name = 'once');

/* insert payment setting */
INSERT INTO payment.paymentssettings
SELECT * FROM (SELECT 1, '82223', '1.6') a
WHERE NOT EXISTS (
SELECT * FROM payment.paymentssettings
WHERE paymentSettingsId = 1);


/* insert payment types */
INSERT INTO payment.paymenttypes
SELECT * FROM (SELECT 1, 'buy', 'Direct payment with money') a
WHERE NOT EXISTS (
SELECT * FROM payment.paymenttypes
WHERE paymentTypeId = 1);

INSERT INTO payment.paymenttypes
SELECT * FROM (SELECT 2, 'pay', 'Pay with money or CB') a
WHERE NOT EXISTS (
SELECT * FROM payment.paymenttypes
WHERE paymentTypeId = 2);

INSERT INTO payment.paymenttypes
SELECT * FROM (SELECT 3, 'free', 'Free gift from admin or etc') a
WHERE NOT EXISTS (
SELECT * FROM payment.paymenttypes
WHERE paymentTypeId = 3);

/*insert transaction types*/
INSERT INTO payment.transactiontypes
SELECT * FROM (SELECT 1, 'direct') a
WHERE NOT EXISTS (
SELECT * FROM payment.transactiontypes
WHERE transactionTypeId = 1);

INSERT INTO payment.transactiontypes
SELECT * FROM (SELECT 2, 'clubby') a
WHERE NOT EXISTS (
SELECT * FROM payment.transactiontypes
WHERE transactionTypeId = 2);


INSERT INTO payment.transactiontypes
SELECT * FROM (SELECT 3, 'free') a
WHERE NOT EXISTS (
SELECT * FROM payment.transactiontypes
WHERE transactionTypeId = 3);

/* insert payments*/
INSERT INTO payment.payments
SELECT * FROM (SELECT 1, 1,'EUR', 'Yearly membership payment', 1, true, true, 2) a
WHERE NOT EXISTS (
SELECT * FROM payment.payments
WHERE paymentId = 1);

INSERT INTO payment.payments
SELECT * FROM (SELECT 2, 2,'EUR', 'Buy 100 Clubby coins', 1, true, false, 0) a
WHERE NOT EXISTS (
SELECT * FROM payment.payments
WHERE paymentId = 2);

INSERT INTO payment.payments
SELECT * FROM (SELECT 3, 1, 'EUR', 'Monthly payment for no reason', 1, true, true, 1) a
WHERE NOT EXISTS (
SELECT * FROM payment.payments
WHERE paymentId = 3);

INSERT INTO payment.payments
SELECT * FROM (SELECT 4, 3, 'EUR', '10 CB gift', 1, true, false, 0) a
WHERE NOT EXISTS (
SELECT * FROM payment.payments
WHERE paymentId = 4);

INSERT INTO payment.lineitems
SELECT * FROM (SELECT nextval('lineitems_id_seq'),'Membership payment', 10000, 1, 1) a
WHERE NOT EXISTS (
SELECT * FROM payment.lineitems
WHERE payment_id = 1);

INSERT INTO payment.lineitems
SELECT * FROM (SELECT nextval('lineitems_id_seq'),'Buy clubby coins', 10000, 1, 2) a
WHERE NOT EXISTS (
SELECT * FROM payment.lineitems
WHERE payment_id = 2);

INSERT INTO payment.lineitems
SELECT * FROM (SELECT nextval('lineitems_id_seq'),'No reason payment', 1000, 1, 3) a
WHERE NOT EXISTS (
SELECT * FROM payment.lineitems
WHERE payment_id = 3);

INSERT INTO payment.lineitems
SELECT * FROM (SELECT nextval('lineitems_id_seq'),'Gift', 1000, 1, 4) a
WHERE NOT EXISTS (
SELECT * FROM payment.lineitems
WHERE payment_id = 4);