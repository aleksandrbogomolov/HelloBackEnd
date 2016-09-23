DELETE FROM contacts;
ALTER SEQUENCE global_seq RESTART WITH 10;

INSERT INTO contacts (name) VALUES ('Aleksandr');
INSERT INTO contacts (name) VALUES ('Boris');
INSERT INTO contacts (name) VALUES ('Fedor');
INSERT INTO contacts (name) VALUES ('Grigoriy');
INSERT INTO contacts (name) VALUES ('Petrov');
INSERT INTO contacts (name) VALUES ('Ivanov');
INSERT INTO contacts (name) VALUES ('Vasechkin');