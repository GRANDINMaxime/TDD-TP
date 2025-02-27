package com.example.services;

import com.example.models.*;
import com.example.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservationServiceTest {

    private ReservationService reservationService;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        reservationService = new ReservationService(reservationRepository, memberRepository, bookService, bookRepository);
    }

    @Test
    void testCheckAvailability_Success() {

        String isbn = "9782853006322";
        LocalDate reservationDate = LocalDate.of(2025, 3, 1);

        Book book = new Book(isbn, "La Genèse", "Moïse", "Société Biblique Française", Format.BROCHE, true);

        when(bookRepository.findByIsbn(isbn)).thenReturn(book);
        when(reservationRepository.findByBookAndReservationDate(book, reservationDate)).thenReturn(null);

        boolean result = reservationService.checkAvailability(isbn, reservationDate);

        assertTrue(result);
    }

    @Test
    void testCheckAvailability_ReservationAlreadyExists() {

        String isbn = "9782853006322";
        LocalDate reservationDate = LocalDate.of(2025, 3, 1);

        Book book = new Book(isbn, "La Genèse", "Moïse", "Société Biblique Française", Format.BROCHE, true);
        Reservation existingReservation = new Reservation(1L, book, null, reservationDate, LocalDate.now(), ReservationStatus.ACTIVE);

        when(bookRepository.findByIsbn(isbn)).thenReturn(book);
        when(reservationRepository.findByBookAndReservationDate(book, reservationDate)).thenReturn(existingReservation);

        boolean result = reservationService.checkAvailability(isbn, reservationDate);

        assertFalse(result);
    }

    @Test
    void testCheckAvailability_ReservationDateAfterMaxAllowed() {
  
        String isbn = "9782853006322";
        LocalDate reservationDate = LocalDate.now().plusMonths(5); 

        Book book = new Book(isbn, "La Genèse", "Moïse", "Société Biblique Française", Format.BROCHE, true);

        when(bookRepository.findByIsbn(isbn)).thenReturn(book);

        boolean result = reservationService.checkAvailability(isbn, reservationDate);

        assertFalse(result);
    }

    @Test
    void testCheckAvailability_BookNotFound() {

        String isbn = "978285300632";
        when(bookRepository.findByIsbn(isbn)).thenReturn(null);

        boolean result = reservationService.checkAvailability(isbn, LocalDate.now());

        assertFalse(result);

        verify(bookRepository).findByIsbn(isbn);
    }

}
