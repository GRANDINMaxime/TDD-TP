package com.example.services;

import java.util.List;
import java.util.Optional;

import com.example.models.Book;
import com.example.repositories.BookRepository;

public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAll(){
        return bookRepository.getAllBook();
    }

    public Book addBook(Book book) {
        return bookRepository.save(book);
    }
    
    public Book updateBook(Book book) {
        if (book == null || book.getIsbn() == null || book.getIsbn().isEmpty()) {
            throw new IllegalArgumentException("Book object or ISBN cannot be null or empty");
        }

        Book existingBook = bookRepository.findByIsbn(book.getIsbn());
        if (existingBook == null) {
            throw new IllegalArgumentException("Book with ISBN " + book.getIsbn() + " not found");
        }

        existingBook.setTitle(book.getTitle());
        existingBook.setAuthor(book.getAuthor());
        existingBook.setPublisher(book.getPublisher());
        existingBook.setFormat(book.getFormat());
        existingBook.setAvailable(book.isAvailable());

        bookRepository.save(existingBook);

        return existingBook;
    }
    public void deleteBook(Book book) {
        if (book == null || book.getIsbn() == null || book.getIsbn().isEmpty()) {
            throw new IllegalArgumentException("Book object or ISBN cannot be null or empty");
        }

        Book existingBook = bookRepository.findByIsbn(book.getIsbn());
        if (existingBook == null) {
            throw new IllegalArgumentException("Book with ISBN " + book.getIsbn() + " not found");
        }

        bookRepository.delete(existingBook);
    }
}
