CREATE TABLE IF NOT EXISTS registration (
  handle_id serial PRIMARY KEY,
  handle varchar(45) NOT NULL UNIQUE,
  secret varchar(450) NOT NULL,
  enabled integer NOT NULL DEFAULT '1'
);