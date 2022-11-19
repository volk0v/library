package me.volkovd.library.controllers;

import me.volkovd.library.dao.BookDAO;
import me.volkovd.library.models.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BooksController {

    private final BookDAO bookDAO;

    @Autowired
    public BooksController(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }

    @GetMapping()
    public String getPageWithAllBooks(Model model) {
        List<Book> books = bookDAO.getAll();

        model.addAttribute("books", books);

        return "books/index";
    }

    @GetMapping("/{id}")
    public String getPageOfBook(@PathVariable("id") int id, Model model) {
        Optional<Book> foundBook = bookDAO.getById(id);

        if (foundBook.isEmpty())
            return "redirect:/books";

        model.addAttribute("book", foundBook.get());

        return "books/show";
    }

    @PostMapping()
    public String saveBook(@ModelAttribute("book") Book book) {
        bookDAO.save(book);

        return "redirect:/books";
    }

}
