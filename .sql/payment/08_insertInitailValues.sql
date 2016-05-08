/* insert transaction types */
INSERT INTO payment.transactiontypes
SELECT * FROM (SELECT 1, 'in') a
WHERE NOT EXISTS (
SELECT * FROM payment.transactiontypes
WHERE transactiontypeid = 1 AND name = 'in');

INSERT INTO payment.transactiontypes
SELECT * FROM (SELECT 2, 'out') a
WHERE NOT EXISTS (
SELECT * FROM payment.transactiontypes
WHERE transactiontypeid = 2 AND name = 'out');

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

/* insert payment setting */
INSERT INTO payment.paymentssettings
SELECT * FROM (SELECT 1, '82223', '1.6') a
WHERE NOT EXISTS (
SELECT * FROM payment.paymentssettings
WHERE paymentSettingsId = 1);

/* insert payment types */
INSERT INTO payment.paymenttypes
SELECT * FROM (SELECT 1, 'yearly', 'only one time in years') a
WHERE NOT EXISTS (
SELECT * FROM payment.paymenttypes
WHERE paymentTypeId = 1);

/* insert payments*/
INSERT INTO payment.payments
SELECT * FROM (SELECT 1, 1, 100,'EUR', 'Yearly membership payment', 1, true) a
WHERE NOT EXISTS (
SELECT * FROM payment.payments
WHERE paymentId = 1);