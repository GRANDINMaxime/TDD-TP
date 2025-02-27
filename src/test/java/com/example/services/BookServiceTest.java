package com.example.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import com.example.models.Book;
import com.example.repositories.BookRepository;
import com.example.services.*;
import com.example.models.Format;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class) // Active Mockito avec JUnit 5
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository; // Création du mock du repository
    @Mock
    private ISBNValidator isbnValidator;

    @InjectMocks
    private BookService bookService; // Injection automatique du mock

    @Test
    public void testGetAllBook(){
        List<Book> existingListOfBooks = new ArrayList<>();
        when(bookRepository.getAllBook()).thenReturn(existingListOfBooks);
        List<Book> books= bookService.getAll();
        assertEquals(books, existingListOfBooks);
    }

    @Test
    public void testAddBook_Success() {
 
        Book book = new Book("9782853006322", "La Genèse", "Moïse", "Société Biblique Française", Format.BROCHE, true);

        when(bookRepository.findByIsbn(book.getIsbn())).thenReturn(null);
        when(isbnValidator.validateISBN(book.getIsbn())).thenReturn(true);
        when(bookRepository.save(book)).thenReturn(book);

        Book savedBook = bookService.addBook(book);

        verify(bookRepository).findByIsbn(book.getIsbn());
        verify(bookRepository).save(book);

        assertNotNull(savedBook);
    }

    @Test
    public void testAddBook_InvalidIsbn() {
    
        String invalidISBN = "1234ABCDE";
        Book book = new Book(invalidISBN, "La Genèse", "Moïse", "Société Biblique Française", Format.BROCHE, true);

        when(isbnValidator.validateISBN(book.getIsbn())).thenReturn(false);
 
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> bookService.addBook(book));

        assertEquals("Invalid ISBN: "+invalidISBN, exception.getMessage());

        verify(bookRepository, never()).save(any(Book.class));
    }
    
    @Test
    public void testAddBook_DuplicateISBN() {

        String uniqueIsbn = "1234567890";
        Book book = new Book(uniqueIsbn, "The Lord of the Rings", "J.R.R. Tolkien",
                "Houghton Mifflin Harcourt", Format.BROCHE, false);

        when(bookRepository.findByIsbn(uniqueIsbn)).thenReturn(new Book(uniqueIsbn, "Existing Book", "Author", "Publisher", Format.GRAND_FORMAT, true));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> bookService.addBook(book));
        assertEquals("Duplicate ISBN: " + uniqueIsbn, exception.getMessage());

        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    public void testUpdateBook_ExistingBook() {
        Book existingBook = new Book("140274577X", "Book Title", "Author", "Publisher", Format.GRAND_FORMAT, true);
        when(bookRepository.findByIsbn(existingBook.getIsbn())).thenReturn(existingBook);

        Book updatedBook = new Book(existingBook.getIsbn(), "New Title", "New Author", "New Publisher", Format.BROCHE, true);

        Book resultBook = bookService.updateBook(updatedBook);

        verify(bookRepository).findByIsbn(existingBook.getIsbn());
        verify(bookRepository).save(existingBook);

        assertEquals(updatedBook.getTitle(), existingBook.getTitle());
        assertEquals(updatedBook.getAuthor(), existingBook.getAuthor());
        assertTrue(existingBook.isAvailable());
        assertEquals(resultBook, existingBook);
    }

    @Test
    public void testDeleteBook_ExistingBook() {
        Book existingBook = new Book("1234567890", "Title", "Author", "Publisher", Format.BROCHE, true);
        when(bookRepository.findByIsbn(existingBook.getIsbn())).thenReturn(existingBook);

        bookService.deleteBook(existingBook);

        verify(bookRepository).findByIsbn(existingBook.getIsbn());
        verify(bookRepository).delete(existingBook);
    }
}

