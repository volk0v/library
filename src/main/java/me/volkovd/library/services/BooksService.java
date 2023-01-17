package me.volkovd.library.services;

import me.volkovd.library.models.Book;
import me.volkovd.library.repositories.BooksRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BooksService {

    private final BooksRepository booksRepository;

    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    List<Book> findAllByOwnerId(int id) {
        return booksRepository.findAllByOwnerId(id);
    }

}
