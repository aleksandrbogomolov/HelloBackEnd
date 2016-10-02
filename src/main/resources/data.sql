DELETE FROM contacts;
DELETE FROM rates;
ALTER SEQUENCE global_seq RESTART WITH 10;

INSERT INTO contacts (name) VALUES ('Aleksandr');
INSERT INTO contacts (name) VALUES ('Boris');
INSERT INTO contacts (name) VALUES ('Fedor');
INSERT INTO contacts (name) VALUES ('Grigoriy');
INSERT INTO contacts (name) VALUES ('Petrov');
INSERT INTO contacts (name) VALUES ('Ivanov');
INSERT INTO contacts (name) VALUES ('Vasechkin');
INSERT INTO contacts (name) VALUES ('Aleksandr1');
INSERT INTO contacts (name) VALUES ('Boris1');
INSERT INTO contacts (name) VALUES ('Fedor1');
INSERT INTO contacts (name) VALUES ('Grigoriy1');
INSERT INTO contacts (name) VALUES ('Petrov1');
INSERT INTO contacts (name) VALUES ('Ivanov1');
INSERT INTO contacts (name) VALUES ('Vasechkin1');
INSERT INTO contacts (name) VALUES ('Aleksandr2');
INSERT INTO contacts (name) VALUES ('Boris2');
INSERT INTO contacts (name) VALUES ('Fedor2');
INSERT INTO contacts (name) VALUES ('Grigoriy2');
INSERT INTO contacts (name) VALUES ('Petrov2');
INSERT INTO contacts (name) VALUES ('Ivanov2');
INSERT INTO contacts (name) VALUES ('Vasechkin2');

INSERT INTO rates (regex, rate) VALUES ('^A.*$', 0);
INSERT INTO rates (regex, rate) VALUES ('^.*[ai].*$', 0);
