CREATE TABLE main.configuration (
  key TEXT PRIMARY KEY NOT NULL,
  value TEXT,
  description TEXT
);
  
INSERT INTO main.configuration(key, value, description) VALUES ('min_recommendation_required', '2', 'Max recommendation requests.');
INSERT INTO main.configuration(key, value, description) VALUES ('max_recommendation_request', '5', 'Min recommendation required to become member.');
INSERT INTO main.configuration(key, value, description) VALUES ('default_user_picture_url', 'http://res.cloudinary.com/teamasdasd/image/upload/v1463936558/_default_avatar.jpg', 'Default picture for users without picture.');
INSERT INTO main.configuration(key, value, description) VALUES ('groups_count', '4', 'Reservations groups count');
