package com.example.services;

import java.util.List;
import com.example.models.Book;
import com.example.repositories.BookRepository;

public class BookService {

    private final BookRepository bookRepository;
    private final ISBNValidator isbnValidator;
    private BookWebService webServiceResponse;

    public BookService(BookRepository bookRepository, ISBNValidator isbnValidator, BookWebService webServiceResponse) {
        this.bookRepository = bookRepository;
        this.isbnValidator = isbnValidator;
        this.webServiceResponse = webServiceResponse;
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
    
        if (!bookIsValid(book)) {
            throw new IllegalArgumentException("Invalid ISBN: " + book.getIsbn());
        }
    
        Book existingBook = bookRepository.findByIsbn(book.getIsbn());
        if (existingBook != null) {
            throw new IllegalArgumentException("Duplicate ISBN: " + book.getIsbn());
        }
    
        if (book.getTitle() == null || book.getAuthor() == null || book.getPublisher() == null) {
            book = retrieveMissingInformation(book);
            if (book == null) {
                throw new IllegalArgumentException("Missing book information and no data found in web service");
            }
        }

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
    public Book retrieveMissingInformation(Book book) { // Consider adding exception for web service errors
        // Check if any fields are missing
        if (book.getTitle() == null || book.getAuthor() == null || book.getPublisher() == null) {
            // Retrieve book information from web service using ISBN from the book
            Book webService = webServiceResponse.getBookByISBN(book.getIsbn());

            if(webService != null){
                // Update book fields with web service data
                book.setTitle(webService.getTitle());
                book.setAuthor(webService.getAuthor());
                book.setPublisher(webService.getPublisher());
            }
            else{
                return null;
            }

        }

        return book;
    }
}
