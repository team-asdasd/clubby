CREATE TABLE main.configuration (
  key TEXT PRIMARY KEY NOT NULL,
  value TEXT,
  description TEXT
);
  
INSERT INTO main.configuration (key, value, description) VALUES ('min_recommendation_required', '2', 'Min recommendation required to become member.');
INSERT INTO main.configuration (key, value, description) VALUES ('default_user_picture_url', 'http://res.cloudinary.com/teamasdasd/image/upload/v1463936558/_default_avatar.jpg', 'Default picture for users without picture.');
INSERT INTO main.configuration (key, value, description) VALUES ('max_recommendation_request', '5', 'Max recommendation requests.');
INSERT INTO main.configuration (key, value, description) VALUES ('groups_count', '6', 'Reservations groups count');
INSERT INTO main.configuration (key, value, description) VALUES ('payment_delay_in_minutes', '20', 'Payment timeout in minutes');
INSERT INTO main.configuration (key, value, description) VALUES ('days_before_cancellation_period_ends', '14', 'Days before reservation start when it cannot be cancelled.');
