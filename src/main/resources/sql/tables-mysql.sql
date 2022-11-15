CREATE TABLE IF NOT EXISTS person (
    person_id int auto_increment PRIMARY KEY,
    full_name varchar(150) NOT NULL UNIQUE,
    birth_year int NOT NULL CHECK (birth_year >= 0)
);
