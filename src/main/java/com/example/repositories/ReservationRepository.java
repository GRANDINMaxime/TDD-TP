package com.example.repositories;

import java.time.LocalDate;

import com.example.models.Book;
import com.example.models.Reservation;

public interface ReservationRepository {
    
    Reservation save(Reservation reservation);

    Reservation findByBookAndReservationDate(Book book, LocalDate reservationDate);

    int countByMemberIdAndEndDateIsNull(String memberId);

    Reservation findById(int reservationId);

}
