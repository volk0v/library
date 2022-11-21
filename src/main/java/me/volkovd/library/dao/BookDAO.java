package me.volkovd.library.dao;

import me.volkovd.library.models.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class BookDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Book> getAll() {
        return jdbcTemplate.query(
                "SELECT book_id AS id, title, author_name, year_of_publication, " +
                        "COALESCE(person_id, 0) AS person_id FROM book",
                new BeanPropertyRowMapper<>(Book.class)
        );
    }

    public Optional<Book> getById(int id) {
        List<Book> queryResult = jdbcTemplate.query(
                "SELECT book_id AS id, title, author_name, year_of_publication, " +
                        "COALESCE(person_id, 0) AS person_id FROM book WHERE book_id=?",
                new BeanPropertyRowMapper<>(Book.class),
                id
        );

        return queryResult.stream().findAny();
    }

    public void save(Book book) {
        jdbcTemplate.update(
                "INSERT INTO book(title, author_name, year_of_publication) VALUES (?,?,?)",
                book.getTitle(),
                book.getAuthorName(),
                book.getYearOfPublication()
        );
    }

    public void update(Book updatedBook, int id) {
        Optional<Book> foundBook = getById(id);

        if (foundBook.isEmpty()) return;

        jdbcTemplate.update(
                "UPDATE book SET title=?, author_name=?, year_of_publication=? WHERE book_id=?",
                updatedBook.getTitle(),
                updatedBook.getAuthorName(),
                updatedBook.getYearOfPublication(),
                id
        );
    }

    public void deleteById(int id) {
        jdbcTemplate.update(
                "DELETE FROM book WHERE book_id=?",
                id
        );
    }

}
