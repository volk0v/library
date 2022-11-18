package me.volkovd.library.dao;

import me.volkovd.library.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PersonDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Person> index() {
        return jdbcTemplate.query(
                "SELECT *, person_id AS id FROM person",
                new BeanPropertyRowMapper<>(Person.class)
        );
    }

    public Optional<Person> show(int id) {
        List<Person> queryResult = jdbcTemplate.query(
                "SELECT * FROM person WHERE person_id=?",
                new BeanPropertyRowMapper<>(Person.class),
                id
        );

        Optional<Person> result = queryResult.stream().findAny();
        result.ifPresent(person -> person.setId(id));

        return result;
    }

    public void save(Person person) {
        jdbcTemplate.update(
                "INSERT INTO person(full_name, birth_year) VALUES (?, ?)",
                person.getFullName(),
                person.getBirthYear()
        );
    }

    public void update(Person updatedPerson, int id) {
        Optional<Person> personToBeUpdated = show(id);

        if (personToBeUpdated.isEmpty()) return;

        jdbcTemplate.update(
                "UPDATE person SET full_name=?, birth_year=? WHERE person_id=?",
                updatedPerson.getFullName(),
                updatedPerson.getBirthYear(),
                id
        );
    }

}
