INSERT INTO USERS (EMAIL, FIRST_NAME, LAST_NAME, PASSWORD)
VALUES ('user@yandex.ru', 'User', 'UserLast', 'password'),
       ('admin@javaops.ru', 'Admin', 'AdminLast', 'admin');

INSERT INTO USER_ROLE (ROLE, USER_ID)
VALUES ('ROLE_USER', 1),
       ('ROLE_ADMIN', 2),
       ('ROLE_USER', 2);

INSERT INTO VOTE (USER_ID, DATE, TIME)
VALUES (1, CURRENT_DATE, CURRENT_TIME),
       (1, '2018-08-07', '12:45:00'),
       (1, '2018-08-06', '10:25:00'),
       (1, '2018-08-05', '09:15:00'),
       (1, '2018-08-04', '15:55:00'),
       (1, '2018-08-03', '11:50:00'),
       (1, '2018-08-02', '11:51:00'),
       (1, '2018-08-01', '11:52:00'),
       (1, '2018-07-31', '11:53:00'),
       (1, '2018-07-30', '11:54:00'),
       (2, CURRENT_DATE, '07:00:00'),
       (2, '2018-08-06', '14:25:00'),
       (2, '2018-08-05', '08:15:00'),
       (2, '2018-08-04', '12:55:00'),
       (2, '2018-07-29', '11:25:00'),
       (2, '2018-07-28', '08:15:00'),
       (2, '2018-07-27', '12:55:00');
