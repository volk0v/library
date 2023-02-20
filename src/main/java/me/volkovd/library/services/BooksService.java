package me.volkovd.library.services;

import me.volkovd.library.models.Book;
import me.volkovd.library.models.Person;
import me.volkovd.library.repositories.BooksRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BooksService {

    private final BooksRepository booksRepository;
    private final PeopleService peopleService;

    public BooksService(BooksRepository booksRepository, PeopleService peopleService) {
        this.booksRepository = booksRepository;
        this.peopleService = peopleService;
    }

    public List<Book> findAll() {
        return booksRepository.findAll();
    }

    public List<Book> findAll(int pageNumber, int booksPerPage) {
        Page<Book> page = booksRepository.findAll(PageRequest.of(pageNumber, booksPerPage));
        return page.getContent();
    }

    public List<Book> findAllByOwnerId(int id) {
        return booksRepository.findAllByOwnerId(id);
    }

    public Optional<Book> findById(int id) {
        return booksRepository.findById(id);
    }

    public void save(Book book) {
        booksRepository.save(book);
    }

    public void update(Book updatedBook, int id) {
        updatedBook.setId(id);

        booksRepository.save(updatedBook);
    }

    public void deleteById(int id) {
        booksRepository.deleteById(id);
    }

    public void assign(int bookId, int personId) {
        Optional<Book> bookOpt = booksRepository.findById(bookId);
        if (bookOpt.isEmpty()) return;
        Book book = bookOpt.get();

        if (book.getOwner() != null) return;

        Optional<Person> personOpt = peopleService.findById(personId);
        if (personOpt.isEmpty()) return;
        Person person = personOpt.get();

        person.addBook(book);
    }

    public void free(int id) {
        Optional<Book> bookOpt = booksRepository.findById(id);
        if (bookOpt.isEmpty()) return;
        Book book = bookOpt.get();

        Person owner = book.getOwner();
        if (owner == null) return;

        owner.removeBook(book);

        update(book, id);
    }

    public int getPagesNumber(int booksPerPage) {
        int amount = (int) booksRepository.count();

        return amount / booksPerPage;
    }

}
