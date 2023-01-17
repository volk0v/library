package me.volkovd.library.services;

import me.volkovd.library.models.Person;
import me.volkovd.library.repositories.PeopleRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PeopleService {

    private final PeopleRepository peopleRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> findAll() {
        return peopleRepository.findAll();
    }

    public Optional<Person> findById(int id) {
        return peopleRepository.findById(id);
    }

    public Optional<Person> findFirstByFullName(String fullName) {
        return peopleRepository.findFirstByFullName(fullName);
    }

    public void save(Person person) {
        peopleRepository.save(person);
    }

    public void update(Person updatedPerson, int id) {
        updatedPerson.setId(id);

        peopleRepository.save(updatedPerson);
    }

    public void deleteById(int id) {
        peopleRepository.deleteById(id);
    }

    @Transactional
    public Optional<Person> findPersonByIdWithInitializedBooks(int id) {
        Optional<Person> personOptional = findById(id);

        if (personOptional.isEmpty()) return personOptional;

        Person person = personOptional.get();
        Hibernate.initialize(person.getBooks());

        return Optional.of(person);
    }

}
