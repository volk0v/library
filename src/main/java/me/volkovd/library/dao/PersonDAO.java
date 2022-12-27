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

    public Optional<Person> getById(int id) {
        List<Person> result = jdbcTemplate.query(
                "SELECT *, person_id AS id FROM person WHERE person_id=?",
                new BeanPropertyRowMapper<>(Person.class),
                id
        );

        return result.stream().findAny();
    }

    public Optional<Person> getByFullName(String fullName) {
        List<Person> result = jdbcTemplate.query(
                "SELECT *, person_id AS id FROM person WHERE full_name=?",
                new BeanPropertyRowMapper<>(Person.class),
                fullName
        );

        return result.stream().findAny();
    }

    public void save(Person person) {
        jdbcTemplate.update(
                "INSERT INTO person(full_name, birth_year) VALUES (?, ?)",
                person.getFullName(),
                person.getBirthYear()
        );
    }

    public void update(Person updatedPerson, int id) {
        Optional<Person> personToBeUpdated = getById(id);

        if (personToBeUpdated.isEmpty()) return;

        jdbcTemplate.update(
                "UPDATE person SET full_name=?, birth_year=? WHERE person_id=?",
                updatedPerson.getFullName(),
                updatedPerson.getBirthYear(),
                id
        );
    }

    public void delete(int id) {
        jdbcTemplate.update(
                "DELETE FROM person WHERE person_id=?",
                id
        );
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
