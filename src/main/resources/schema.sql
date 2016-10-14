DROP TABLE IF EXISTS contacts CASCADE;

CREATE TABLE contacts (
  id   BIGSERIAL    NOT NULL PRIMARY KEY,
  name VARCHAR(100) NOT NULL
);

CREATE OR REPLACE FUNCTION get_forward(BIGINT)
  RETURNS REFCURSOR AS 'DECLARE ref REFCURSOR;
BEGIN
  OPEN ref FOR SELECT *
               FROM contacts
               WHERE id > $1
               ORDER BY id;
  RETURN ref;
END;' LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_back(BIGINT)
  RETURNS REFCURSOR AS 'DECLARE ref REFCURSOR;
BEGIN
  OPEN ref FOR SELECT *
               FROM contacts
               WHERE id < $1
               ORDER BY id DESC;
  RETURN ref;
END;' LANGUAGE plpgsql;
