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
SELECT * FROM (SELECT 1, 'direct', 'Direct payment with money') a
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
SELECT * FROM (SELECT 1, 1, 100,'EUR', 'Yearly membership payment', 1, true) a
WHERE NOT EXISTS (
SELECT * FROM payment.payments
WHERE paymentId = 1);