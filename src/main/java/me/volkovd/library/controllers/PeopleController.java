package me.volkovd.library.controllers;

import me.volkovd.library.dao.PersonDAO;
import me.volkovd.library.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PersonDAO personDAO;

    @Autowired
    public PeopleController(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("people", personDAO.index());

        return "people/index";
    }

    @GetMapping("/new")
    public String getPageForCreatingPerson() {
        return "people/new";
    }

    @PostMapping()
    public String createPerson(@ModelAttribute("person") Person person) {
        personDAO.save(person);

        return "redirect:/people";
    }

    @PatchMapping("/{id}")
    public String editPerson(@ModelAttribute("person") Person person,
                             @PathVariable("id") int id) {
        personDAO.update(person, id);

        return "redirect:/people";
    }

}
