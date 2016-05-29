CREATE SCHEMA notification;

CREATE TABLE notification.notifications
(
    id SERIAL PRIMARY KEY,
    title TEXT NOT NULL,
    action TEXT
);
CREATE TABLE notification.notifications_user
(
    notification_id INT,
    user_id INT,
    is_read BOOLEAN DEFAULT FALSE ,
	argument TEXT,
    CONSTRAINT notifications_user_notifications_id_fk FOREIGN KEY (notification_id) REFERENCES notifications (id),
    CONSTRAINT notifications_user_users_id_fk FOREIGN KEY (user_id) REFERENCES main.users (id),
    CONSTRAINT pk_notifications_user PRIMARY KEY (notification_id,user_id)
)