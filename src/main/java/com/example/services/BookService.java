package com.example.services;

import java.util.List;
import com.example.models.Book;
import com.example.repositories.BookRepository;

public class BookService {

    private final BookRepository bookRepository;
    private final ISBNValidator isbnValidator;

    public BookService(BookRepository bookRepository, ISBNValidator isbnValidator) {
        this.bookRepository = bookRepository;
        this.isbnValidator = isbnValidator;
    }

    public List<Book> getAll(){
        return bookRepository.getAllBook();
    }

    public boolean bookIsValid(Book book) {
        return this.isbnValidator.validateISBN(book.getIsbn());
    }

    public Book addBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }
    
        // Vérifier l'ISBN
        if (!bookIsValid(book)) {
            throw new IllegalArgumentException("Invalid ISBN: " + book.getIsbn());
        }
    
        // Vérifier la présence du livre en base
        Book existingBook = bookRepository.findByIsbn(book.getIsbn());
        if (existingBook != null) {
            throw new IllegalArgumentException("Duplicate ISBN: " + book.getIsbn());
        }
    
        // Sauvegarde du livre
        bookRepository.save(book);
        return book;
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
