package me.volkovd.library.services;

import me.volkovd.library.models.Book;
import me.volkovd.library.models.Person;
import me.volkovd.library.repositories.BooksRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class BooksService {

    public enum SortableField {
        YEAR("yearOfPublication");

        private final String fieldName;

        SortableField(String fieldName) {
            this.fieldName = fieldName;
        }

        @Override
        public String toString() {
            return fieldName;
        }
    }

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
        if (pageNumber < 0 || booksPerPage <= 0) {
            return Collections.emptyList();
        }

        Page<Book> page = booksRepository.findAll(PageRequest.of(pageNumber, booksPerPage));
        return page.getContent();
    }

    public List<Book> findAll(int pageNumber, int booksPerPage, SortableField field) {
        Page<Book> page = booksRepository.findAll(PageRequest.of(pageNumber, booksPerPage, Sort.by(field.toString())));
        return page.getContent();
    }

    public List<Book> findAllWithSorting(SortableField field) {
        return booksRepository.findAll(Sort.by(field.toString()));
    }

    public Optional<Book> findById(int id) {
        return booksRepository.findById(id);
    }

    public List<Book> findAllByTitleContains(String title) {
        return booksRepository.findAllByTitleContains(title);
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

    @Transactional
    public void assign(int bookId, int personId) {
        Optional<Book> bookOpt = booksRepository.findById(bookId);
        if (bookOpt.isEmpty()) return;
        Book book = bookOpt.get();

        if (book.getOwner() != null) return;

        Optional<Person> personOpt = peopleService.findById(personId);
        if (personOpt.isEmpty()) return;
        Person person = personOpt.get();

        LocalDate currentDate = LocalDate.now();
        LocalDate delayDate = currentDate.plusDays(10);
        book.setDelayDate(delayDate);

        person.addBook(book);
    }

    @Transactional
    public void free(int id) {
        Optional<Book> bookOpt = booksRepository.findById(id);
        if (bookOpt.isEmpty()) return;
        Book book = bookOpt.get();

        Person owner = book.getOwner();
        if (owner == null) return;

        owner.removeBook(book);
        book.setDelayDate(null);

        update(book, id);
    }

    public int getPagesNumber(int booksPerPage) {
        int amount = (int) booksRepository.count();

        return (int) Math.ceil((double) amount / (double) booksPerPage);
    }

}
