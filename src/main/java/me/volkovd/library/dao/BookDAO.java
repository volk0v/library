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
                "SELECT *, book_id AS id FROM book",
                new BeanPropertyRowMapper<>(Book.class)
        );
    }

    public Optional<Book> getById(int id) {
        List<Book> queryResult = jdbcTemplate.query(
                "SELECT *, book_id AS id FROM book WHERE book_id=?",
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

}
