package me.volkovd.library.validators;

import me.volkovd.library.dao.PersonDAO;
import me.volkovd.library.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
public class PersonValidator implements Validator {

    private final PersonDAO personDAO;

    @Autowired
    public PersonValidator(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person targetPerson = (Person) target;

        Optional<Person> foundPerson = personDAO.show(targetPerson.getFullName());
        if (foundPerson.isPresent()) {
            Person person = foundPerson.get();

            if (person.getId() != targetPerson.getId())
                errors.rejectValue("fullName", "", "Человек с таким именем уже существует");
        }
    }

}
