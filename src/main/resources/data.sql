insert into users (username, password, email, age)
values ('admin','$2a$12$q2zxI90RS1jpXmLqmlkPsuc299b7tWaf9Ar1zHktlJSBH/sLafUwe' ,'admin@mail.com', 30),
       ('user', '$2a$12$0uMBB71uUNC.0wrt7.phd.6AlTI6xmUSVxyA.hM8pFLM0af3jhNTG', 'user@mail.com', 25);

SET @admin_id = (SELECT id FROM users WHERE username = 'admin');
SET @user_id = (SELECT id FROM users WHERE username = 'user');

insert into roles (role) values ('ROLE_ADMIN'), ('ROLE_USER');

SET @role_admin_id = (SELECT id FROM roles WHERE role = 'ROLE_ADMIN');
SET @role_user_id = (SELECT id FROM roles WHERE role = 'ROLE_USER');

insert into users_roles(user_id, role_id) values (@admin_id, @role_admin_id), (@admin_id, @role_user_id), (@user_id, @role_user_id);