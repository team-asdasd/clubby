CREATE TABLE main.form_results
(
    id SERIAL PRIMARY KEY,
    user_id INT,
    field INT NOT NULL,
    value TEXT,
    CONSTRAINT form_results_users_id_fk FOREIGN KEY (user_id) REFERENCES main.users (id),
    CONSTRAINT form_results_formfields_id_fk FOREIGN KEY (field) REFERENCES main.form_fields (id)
);