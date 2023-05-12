INSERT INTO roles (id, name) VALUES (0, 'ROLE_USER');
INSERT INTO roles (id, name) VALUES (1, 'ROLE_ADMIN');

INSERT INTO users (id, username, email, password) VALUES (0, 'admin', 'admin@gmail.com', '$2a$10$jlg0PfLXrA1buYNqI/FuN.6Cn1pUXojOMjwKCPqK0qW4XlWnlil/e');

INSERT INTO user_roles (user_id, role_id) VALUES (0, 1);


--ROLES
--	ROLE_NAME
--	REMARKS
--	ID
--	NAME
--USERS
--	USER_NAME
--	IS_ADMIN
--	REMARKS
--	ID
--	EMAIL
--	PASSWORD
--	USERNAME
--USER_ROLES
--	USER_ID
--	ROLE_ID
