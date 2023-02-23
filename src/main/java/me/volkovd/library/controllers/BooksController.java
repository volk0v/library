package me.volkovd.library.controllers;

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

    private final String defaultPage = "/books?page=0&books_per_page=3&sort_by_year=true";

    private final BooksService booksService;
    private final PeopleService peopleService;

    @Autowired
    public BooksController(BooksService booksService, PeopleService peopleService) {
        this.booksService = booksService;
        this.peopleService = peopleService;
    }

    @GetMapping()
    public String getPageWithAllBooks(@RequestParam(name = "page", defaultValue = "-1") int pageNumber,
                                      @RequestParam(name = "books_per_page", defaultValue = "-1") int booksPerPage,
                                      @RequestParam(name = "doesNeedSortingByYear", defaultValue = "false") boolean doesNeedSortingByYear,
                                      Model model) {
        List<Book> books;

        if (pageNumber != -1 && booksPerPage != -1) {
            if (doesNeedSortingByYear) {
                books = booksService.findAll(pageNumber, booksPerPage, BooksService.SortableField.YEAR_OF_PUBLICATION);
            } else {
                books = booksService.findAll(pageNumber, booksPerPage);
            }

            int pagesAmount = booksService.getPagesNumber(booksPerPage);
            model.addAttribute("pagesAmount", pagesAmount);
            model.addAttribute("booksPerPage", booksPerPage);
        } else {
            if (doesNeedSortingByYear) {
                books = booksService.findAllWithSorting(BooksService.SortableField.YEAR_OF_PUBLICATION);
            } else {
                books = booksService.findAll();
            }
        }

        model.addAttribute("books", books);

        return "books/index";
    }

    @GetMapping("/{id}")
    public String getPageOfBook(@PathVariable("id") int id, Model model) {
        Optional<Book> foundBook = booksService.findById(id);

        if (foundBook.isEmpty())
            return "redirect:" + defaultPage;

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

        booksService.save(book);

        return "redirect:" + defaultPage;
    }

    @GetMapping("/{id}/edit")
    public String getPageOfEditingBook(@PathVariable("id") int id,
                                       Model model) {
        Optional<Book> foundBook = booksService.findById(id);

        if (foundBook.isEmpty()) return "redirect:" + defaultPage;

        model.addAttribute("book", foundBook.get());

        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String editBook(@ModelAttribute("book") @Valid Book book,
                           BindingResult bindingResult,
                           @PathVariable("id") int id) {
        if (bindingResult.hasErrors())
            return "books/edit";

        booksService.update(book, id);

        return "redirect:" + defaultPage;
    }

    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable("id") int id) {
        booksService.deleteById(id);

        return "redirect:" + defaultPage;
    }

    @PatchMapping("/{bookId}/assign")
    public String assignBook(@PathVariable("bookId") int bookId,
                             @ModelAttribute("personId") int personId) {
        booksService.assign(bookId, personId);

        return "redirect:/books/" + bookId;
    }

    @PatchMapping("/{bookId}/free")
    public String freeBook(@PathVariable("bookId") int bookId) {
        booksService.free(bookId);

        return "redirect:/books/" + bookId;
    }

}
