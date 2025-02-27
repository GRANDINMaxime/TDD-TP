package com.example.repositories;

import java.util.List;
import com.example.models.Book;

public interface BookRepository {
    List<Book> getAllBook();
    
    Book findByIsbn(String isbn);

    Book save(Book book);

    Book delete(Book book);
}
