package me.volkovd.library.controllers;

import me.volkovd.library.dao.PersonDAO;
import me.volkovd.library.models.Person;
import me.volkovd.library.validators.PersonValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PersonDAO personDAO;
    private final PersonValidator personValidator;

    @Autowired
    public PeopleController(PersonDAO personDAO, PersonValidator personValidator) {
        this.personDAO = personDAO;
        this.personValidator = personValidator;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("people", personDAO.getAll());

        return "people/index";
    }

    @GetMapping("/new")
    public String getPageForCreatingPerson(Model model) {
        model.addAttribute("person", new Person());

        return "people/new";
    }

    @PostMapping()
    public String createPerson(@ModelAttribute("person") @Valid Person person,
                               BindingResult bindingResult) {
        personValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors())
            return "people/new";

        personDAO.save(person);

        return "redirect:/people";
    }

    @GetMapping("/{id}")
    public String getPageForPerson(@PathVariable("id") int id,
                                   Model model) {
        Optional<Person> foundPerson = personDAO.show(id);

        if (foundPerson.isEmpty()) return "redirect:/people";

        model.addAttribute("person", foundPerson.get());

        return "people/show";
    }

    @GetMapping("/{id}/edit")
    public String getPageForEditingPerson(@PathVariable("id") int id,
                                          Model model) {
        Optional<Person> foundPerson = personDAO.show(id);

        if (foundPerson.isPresent()) {
            model.addAttribute("person", foundPerson.get());
        } else {
            return "redirect:/people";
        }

        return "people/edit";
    }

    @PatchMapping("/{id}")
    public String editPerson(@ModelAttribute("person") @Valid Person person,
                             BindingResult bindingResult,
                             @PathVariable("id") int id) {
        person.setId(id);
        personValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors())
            return "people/edit";

        personDAO.update(person, id);

        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String deletePerson(@PathVariable("id") int id) {
        personDAO.delete(id);

        return "redirect:/people";
    }

}
