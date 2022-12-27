package me.volkovd.library.dao;

import me.volkovd.library.models.Book;
import me.volkovd.library.models.Person;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class PersonDAO {

    private final JdbcTemplate jdbcTemplate;
    private final SessionFactory sessionFactory;

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate, SessionFactory sessionFactory) {
        this.jdbcTemplate = jdbcTemplate;
        this.sessionFactory = sessionFactory;
    }

    @Transactional(readOnly = true)
    public List<Person> getAll() {
        Session session = sessionFactory.getCurrentSession();

        Query<Person> people = session.createQuery("SELECT p FROM Person p", Person.class);

        return people.getResultList();
    }

    @Transactional(readOnly = true)
    public Optional<Person> getById(int id) {
        Session session = sessionFactory.getCurrentSession();

        Person person = session.get(Person.class, id);

        return Optional.ofNullable(person);
    }

    @Transactional(readOnly = true)
    public Optional<Person> getByFullName(String fullName) {
        Session session = sessionFactory.getCurrentSession();

        Query<Person> query = session.createQuery("SELECT p FROM Person p WHERE p.fullName=?1", Person.class);
        query.setParameter(1, fullName);

        return query.getResultList().stream().findAny();
    }

    @Transactional
    public void save(Person person) {
        Session session = sessionFactory.getCurrentSession();

        session.save(person);
    }

    @Transactional
    public void update(Person updatedPerson, int id) {
        Session session = sessionFactory.getCurrentSession();

        updatedPerson.setId(id);

        session.update(updatedPerson);
    }

    @Transactional
    public void delete(int id) {
        Session session = sessionFactory.getCurrentSession();

        Person person = session.get(Person.class, id);

        if (person == null) return;

        session.delete(person);
    }

    public List<Book> getBooksForPerson(int id) {
        return jdbcTemplate.query(
                "SELECT book_id AS id, title, " +
                        "author_name, year_of_publication, COALESCE(person_id, 0) AS person_id FROM book WHERE person_id=?",
                new BeanPropertyRowMapper<>(Book.class),
                id
        );
    }

}
