CREATE TABLE payment.lineitems
(
    id SERIAL NOT NULL,
    title TEXT,
    price INTEGER,
    quantity INTEGER,
    payment_id INTEGER,
    CONSTRAINT lineitems_pkey PRIMARY KEY (payment_id),
    CONSTRAINT lineitems_payments_paymentid_fk FOREIGN KEY (payment_id) REFERENCES payment.payments (paymentid)
);