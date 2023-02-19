package me.volkovd.library.services;

import me.volkovd.library.models.Book;
import me.volkovd.library.repositories.BooksRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BooksService {

    private final BooksRepository booksRepository;

    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    @Transactional(readOnly = true)
    public List<Book> findAll() {
        return booksRepository.findAll();
    }

    List<Book> findAllByOwnerId(int id) {
        return booksRepository.findAllByOwnerId(id);
    }

    @Transactional(readOnly = true)
    public Optional<Book> findById(int id) {
        return booksRepository.findById(id);
    }

}
