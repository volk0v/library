CREATE TABLE IF NOT EXISTS person (
    person_id int auto_increment PRIMARY KEY,
    full_name varchar(150) NOT NULL UNIQUE,
    birth_year int NOT NULL CHECK (birth_year >= 0)
);

CREATE TABLE IF NOT EXISTS book (
    book_id int auto_increment PRIMARY KEY,
    title varchar(300) NOT NULL,
    author_name varchar(150) NOT NULL,
    year_of_publication int NOT NULL
)
