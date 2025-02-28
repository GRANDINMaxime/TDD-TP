package com.example.services;

import com.example.models.*;
import com.example.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
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

    @Mock
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        reservationService = new ReservationService(reservationRepository, memberRepository, bookService, bookRepository
        //, emailService
        );
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

    @Test
    void testMakeReservation_Successful() {
        String isbn = "9782853006322";
        LocalDate reservationDate = LocalDate.now();

        Book book = new Book(isbn, "La Genèse", "Moïse", "Société Biblique Française", Format.BROCHE, true);
        Member member = new Member(8L, "5", "GRANDIN", "Maxime", LocalDate.now().minusYears(25), Gender.MONSIEUR, "lala@lala.fr");
        Reservation previousReservation = new Reservation(3L, book, member, reservationDate.minusDays(1), null, ReservationStatus.ACTIVE);

        when(bookRepository.findByIsbn(isbn)).thenReturn(book);
        when(memberRepository.findById("memberId")).thenReturn(member);
        when(reservationRepository.countByMemberIdAndEndDateIsNull("memberId")).thenReturn(2);
        when(reservationRepository.findByMemberIdAndEndDateIsNull("memberId")).thenReturn(List.of(previousReservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(new Reservation(4L, book, member, reservationDate, LocalDate.now(), ReservationStatus.ACTIVE));

        Reservation reservation = reservationService.makeReservation(isbn, reservationDate, "memberId");

        assertNotNull(reservation);
        verify(reservationRepository).save(any(Reservation.class));
        verify(reservationRepository).findByMemberIdAndEndDateIsNull("memberId"); // Vérifie l'affichage des réservations
    }

    @Test
    void testMakeReservation_MaxReservationsReached() {
        String isbn = "9782853006322";
        LocalDate reservationDate = LocalDate.now();

        Book book = new Book(isbn, "La Genèse", "Moïse", "Société Biblique Française", Format.BROCHE, true);
        Member member = new Member(8L, "5", "GRANDIN", "Maxime", LocalDate.now().minusYears(25), Gender.MONSIEUR, "lala@lala.fr");

        when(bookRepository.findByIsbn(isbn)).thenReturn(book);
        when(memberRepository.findById("memberId")).thenReturn(member);
        when(reservationRepository.countByMemberIdAndEndDateIsNull("memberId")).thenReturn(3);

        assertThrows(IllegalArgumentException.class, () -> reservationService.makeReservation(isbn, reservationDate, "memberId"));

        verify(reservationRepository, never()).save(any(Reservation.class)); // Vérifie que la réservation n'est pas enregistrée
    }

    @Test
    void testCancelReservation_ReservationNotFound() {
        when(reservationRepository.findById(anyInt())).thenReturn(null);

        boolean result = reservationService.cancelReservation(1);

        assertFalse(result);

        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void testCancelReservation_Success() {

        Reservation reservation = new Reservation(1L, 
                                                  new Book("9782853006322", "La Genèse", "Moïse", "Société Biblique Française", Format.BROCHE, true),
                                                  new Member(8L, "5", "GRANDIN", "Maxime", LocalDate.now().minusYears(25), Gender.MONSIEUR, "lala@lala.fr"),
                                                  LocalDate.now().minusDays(10),
                                                  null,
                                                  ReservationStatus.ACTIVE);

        when(reservationRepository.findById(1)).thenReturn(reservation);

        when(reservationRepository.save(reservation)).thenReturn(reservation);

        boolean result = reservationService.cancelReservation(1);

        assertTrue(result);

        assertNotNull(reservation.getDueDate());
        assertEquals(LocalDate.now(), reservation.getDueDate());

        verify(reservationRepository).save(reservation);
    }
}
