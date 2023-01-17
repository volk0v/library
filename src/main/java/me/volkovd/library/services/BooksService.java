package me.volkovd.library.services;

import me.volkovd.library.repositories.BooksRepository;
import org.springframework.stereotype.Service;

@Service
public class BooksService {

    private final BooksRepository booksRepository;

    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

}
