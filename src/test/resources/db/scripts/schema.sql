CREATE TABLE IF NOT EXISTS accident_type (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS rule (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS accident (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    text TEXT NOT NULL,
    address TEXT NOT NULL,
    type_id INT REFERENCES accident_type(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS accident_rule (
    accident_id INT REFERENCES accident(id) ON DELETE CASCADE,
    rule_id INT REFERENCES rule(id) ON DELETE CASCADE,
    PRIMARY KEY (accident_id, rule_id)
);

CREATE TABLE IF NOT EXISTS authorities (
  id serial primary key,
  authority VARCHAR(50) NOT NULL unique
);

CREATE TABLE IF NOT EXISTS users (
  id serial primary key,
  username VARCHAR(50) NOT NULL unique,
  password VARCHAR(100) NOT NULL,
  enabled boolean default true,
  authority_id int not null references authorities(id)
);

ALTER TABLE accident_type ALTER COLUMN id RESTART WITH 1;
ALTER TABLE rule ALTER COLUMN id RESTART WITH 1;
ALTER TABLE accident ALTER COLUMN id RESTART WITH 1;