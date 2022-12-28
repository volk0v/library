package me.volkovd.library.dao;

import me.volkovd.library.models.Book;
import me.volkovd.library.models.Person;
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

    @Transactional
    public void deleteById(int id) {
        Session session = sessionFactory.getCurrentSession();

        Query query = session.createQuery("DELETE FROM Book b WHERE b.id=?1");
        query.setParameter(1, id);

        query.executeUpdate();
    }

    @Transactional
    public void assign(int bookId, int personId) {
        Session session = sessionFactory.getCurrentSession();

        Book book = session.get(Book.class, bookId);

        boolean hasOwner = book.getOwner() != null;
        if (hasOwner) return;

        Person newOwner = session.get(Person.class, personId);

        newOwner.addBook(book);
    }

    @Transactional
    public void free(int bookId) {
        Session session = sessionFactory.getCurrentSession();

        Book book = session.get(Book.class, bookId);

        Person owner = book.getOwner();

        if (owner != null) {
            owner.removeBook(book);
        } else {
            book.setOwner(null);
        }

        session.update(book);
    }

}
