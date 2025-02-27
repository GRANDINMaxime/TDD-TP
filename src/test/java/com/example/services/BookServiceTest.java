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

        Book book = new Book("123456789", "Mockito for Dummies", "John Doe", "TechBooks", Format.BROCHE, true);

        when(bookRepository.save(book)).thenReturn(book);

        Book savedBook = bookService.addBook(book);

        assertNotNull(savedBook);
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

