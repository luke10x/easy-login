CREATE TABLE IF NOT EXISTS handles (
  handle_id serial PRIMARY KEY,
  handle varchar(45) NOT NULL,
  secret varchar(450) NOT NULL,
  enabled integer NOT NULL DEFAULT '1'
)