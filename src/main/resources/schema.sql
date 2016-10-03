DROP TABLE IF EXISTS contacts;
DROP TABLE IF EXISTS rates;
DROP SEQUENCE IF EXISTS global_seq;

CREATE SEQUENCE global_seq START 10;

CREATE TABLE contacts (
  id   BIGINT       NOT NULL PRIMARY KEY DEFAULT nextval('global_seq'),
  name VARCHAR(100) NOT NULL
);

CREATE TABLE rates (
  id     INTEGER      NOT NULL PRIMARY KEY DEFAULT nextval('global_seq'),
  regex  VARCHAR(100) NOT NULL,
  rate INTEGER      NOT NULL
)
