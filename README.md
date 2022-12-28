# Library

It's a CRUD application for library written by using Spring Framework.

## Task

The local library wants to switch to digital accounting of books. You
need to implement a web application for them. Librarians
should be able to register readers, give them
books and release books (after the reader returns
the book back to the library).

## Required environment variables
- **DB_URL** - database URL
- **DB_USERNAME** - username
- **DB_PASSWORD** - password

## Realization

There are two main entities: Person and Book. They need separate
tables in database.

Application uses MySQL database.

### Initialization SQL script

It's written in this [file](src/main/resources/sql/tables-mysql.sql).
Copied it here:

Person table:

```sql
CREATE TABLE IF NOT EXISTS person
(
    person_id  int auto_increment PRIMARY KEY,
    full_name  varchar(150) NOT NULL UNIQUE,
    birth_year int          NOT NULL CHECK (birth_year >= 0)
);
```

Book table:
```sql
CREATE TABLE IF NOT EXISTS book
(
    book_id             int auto_increment PRIMARY KEY,
    person_id           int REFERENCES person (person_id),
    title               varchar(300) NOT NULL,
    author_name         varchar(150) NOT NULL,
    year_of_publication int          NOT NULL
);
```
