package com.example.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.models.*;
import com.example.repositories.*;
import com.example.services.*;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final BookService bookService;
    private final BookRepository bookRepository;
    //private final EmailService emailService;

    public ReservationService(ReservationRepository reservationRepository, MemberRepository memberRepository, BookService bookService, BookRepository bookRepository
    //, EmailService emailService
    ) {
        this.reservationRepository = reservationRepository;
        this.memberRepository = memberRepository;
        this.bookService = bookService;
        this.bookRepository = bookRepository;
        //this.emailService = emailService;
    }
    public boolean checkAvailability(String isbn, LocalDate reservationDate) {
        Book book = bookRepository.findByIsbn(isbn);
        if (book == null) {
            return false;
        }

        Reservation existingReservation = reservationRepository.findByBookAndReservationDate(book, reservationDate);
        if (existingReservation != null) {
            return false;
        }

        LocalDate today = LocalDate.now();
        LocalDate maxReservationDate = today.plusMonths(4);
        if (reservationDate.isBefore(today) || reservationDate.isAfter(maxReservationDate)) {
            return false;
        }
        return true;
    }

    public Reservation makeReservation(String isbn, LocalDate reservationDate, String memberId) {
        if (!checkAvailability(isbn, reservationDate)) {
            return null;
        }

        List<Reservation> activeReservations = reservationRepository.findByMemberIdAndEndDateIsNull(memberId);

        System.out.println("Réservations en cours pour le membre " + memberId + " :");
        for (Reservation res : activeReservations) {
            System.out.println(" - Livre : " + res.getBook().getTitle() + ", Date de réservation : " + res.getReservationDate());
        }

        int openReservationsCount = reservationRepository.countByMemberIdAndEndDateIsNull(memberId);
        if (openReservationsCount >= 3) {
            throw new IllegalArgumentException("Member has reached the maximum number of open reservations");
        }
        Member member = memberRepository.findById(memberId);
        Book book = bookRepository.findByIsbn(isbn);
        Reservation reservation = new Reservation(1L, book,member, reservationDate, LocalDate.now(), ReservationStatus.ACTIVE);
        reservationRepository.save(reservation);

        return reservation;
    }
    


    public boolean cancelReservation(int reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId);
        if (reservation == null) {
            return false;
        }

        if (reservation.getDueDate() != null) {
            return false;
        }

        reservation.setDueDate(LocalDate.now());
        reservationRepository.save(reservation);

        return true;
    }
}
