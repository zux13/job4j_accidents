CREATE TABLE accident (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    text TEXT NOT NULL,
    address TEXT NOT NULL,
    type_id INT REFERENCES accident_type(id) ON DELETE SET NULL
);
