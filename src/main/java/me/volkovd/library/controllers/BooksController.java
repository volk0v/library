package me.volkovd.library.controllers;

import me.volkovd.library.dao.BookDAO;
import me.volkovd.library.models.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

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

}
