package me.volkovd.library.controllers;

import me.volkovd.library.dao.BookDAO;
import me.volkovd.library.models.Book;
import me.volkovd.library.models.Person;
import me.volkovd.library.services.BooksService;
import me.volkovd.library.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BooksController {

    private final BookDAO bookDAO;
    private final BooksService booksService;
    private final PeopleService peopleService;

    @Autowired
    public BooksController(BookDAO bookDAO, BooksService booksService, PeopleService peopleService) {
        this.bookDAO = bookDAO;
        this.booksService = booksService;
        this.peopleService = peopleService;
    }

    @GetMapping()
    public String getPageWithAllBooks(Model model) {
        List<Book> books = booksService.findAll();

        model.addAttribute("books", books);

        return "books/index";
    }

    @GetMapping("/{id}")
    public String getPageOfBook(@PathVariable("id") int id, Model model) {
        Optional<Book> foundBook = booksService.findById(id);

        if (foundBook.isEmpty())
            return "redirect:/books";

        Book book = foundBook.get();

        model.addAttribute("book", book);
        model.addAttribute("people", peopleService.findAll()
        );

        Person bookOwner = book.getOwner();
        if (bookOwner != null) {
            model.addAttribute("bookOwner", bookOwner);
        }

        return "books/show";
    }

    @GetMapping("/new")
    public String getPageOfAddingNewBook(Model model) {
        model.addAttribute("book", new Book());

        return "books/new";
    }

    @PostMapping()
    public String saveBook(@ModelAttribute("book") @Valid Book book,
                           BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "books/new";

        bookDAO.save(book);

        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String getPageOfEditingBook(@PathVariable("id") int id,
                                       Model model) {
        Optional<Book> foundBook = bookDAO.getById(id);

        if (foundBook.isEmpty()) return "redirect:/books";

        model.addAttribute("book", foundBook.get());

        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String editBook(@ModelAttribute("book") @Valid Book book,
                           BindingResult bindingResult,
                           @PathVariable("id") int id) {
        if (bindingResult.hasErrors())
            return "books/edit";

        bookDAO.update(book, id);

        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable("id") int id) {
        bookDAO.deleteById(id);

        return "redirect:/books";
    }

    @PatchMapping("/{bookId}/assign")
    public String assignBook(@PathVariable("bookId") int bookId,
                             @ModelAttribute("personId") int personId) {
        bookDAO.assign(bookId, personId);

        return "redirect:/books/" + bookId;
    }

    @PatchMapping("/{bookId}/free")
    public String freeBook(@PathVariable("bookId") int bookId) {
        bookDAO.free(bookId);

        return "redirect:/books/" + bookId;
    }

}
