package me.volkovd.library.dao;

import me.volkovd.library.models.Book;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class BookDAO {

    private final JdbcTemplate jdbcTemplate;
    private final SessionFactory sessionFactory;

    @Autowired
    public BookDAO(JdbcTemplate jdbcTemplate, SessionFactory sessionFactory) {
        this.jdbcTemplate = jdbcTemplate;
        this.sessionFactory = sessionFactory;
    }

    @Transactional(readOnly = true)
    public List<Book> getAll() {
        Session session = sessionFactory.getCurrentSession();

        Query<Book> query = session.createQuery("SELECT b FROM Book b", Book.class);

        return query.getResultList();
    }

    @Transactional(readOnly = true)
    public Optional<Book> getById(int id) {
        Session session = sessionFactory.getCurrentSession();

        return Optional.ofNullable(session.get(Book.class, id));
    }

    @Transactional
    public void save(Book book) {
        Session session = sessionFactory.getCurrentSession();

        session.save(book);
    }

    @Transactional
    public void update(Book updatedBook, int id) {
        Session session = sessionFactory.getCurrentSession();

        Book foundBook = session.get(Book.class, id);
        if (foundBook == null) return;

        foundBook.setTitle(updatedBook.getTitle());
        foundBook.setAuthorName(updatedBook.getAuthorName());
        foundBook.setYearOfPublication(updatedBook.getYearOfPublication());

        session.update(foundBook);
    }

    public void deleteById(int id) {
        jdbcTemplate.update(
                "DELETE FROM book WHERE book_id=?",
                id
        );
    }

    public void assign(int bookId, int personId) {
        Number personIdFromDb = jdbcTemplate.queryForObject(
                "SELECT person_id FROM book WHERE book_id=?",
                Integer.class,
                bookId
        );

        int foundPersonId = personIdFromDb != null ? personIdFromDb.intValue() : 0;

        if (foundPersonId != 0) return;

        jdbcTemplate.update(
                "UPDATE book SET person_id=? WHERE book_id=?",
                personId,
                bookId
        );
    }

    public void free(int bookId) {
        jdbcTemplate.update(
                "UPDATE book SET person_id=NULL WHERE book_id=?",
                bookId
        );
    }

}
